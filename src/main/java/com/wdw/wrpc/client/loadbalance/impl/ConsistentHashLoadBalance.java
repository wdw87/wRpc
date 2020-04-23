package com.wdw.wrpc.client.loadbalance.impl;

import com.wdw.wrpc.client.registry.Invoker;
import com.wdw.wrpc.common.protocal.ServiceRequestPacket;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一致性hash
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance{
    public static final String NAME = "hash";
    private static final ConcurrentHashMap<String, ConsistentHashSelector> selectors =
            new ConcurrentHashMap<>();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected Invoker doSelect(List<Invoker> invokers, ServiceRequestPacket requestPacket) {
        String serviceName = requestPacket.getClassName();
        String methodName = requestPacket.getMethodName();
        String key = serviceName + methodName;
        //先计算当前服务列表的hashcode
        int identityHashCode = System.identityHashCode(invokers);
        ConsistentHashSelector selector = selectors.get(key);
        //如果hashcode 为空，或者与原先的hashcode不相同，说明服务列表发生了变化，重建selector
        if(selector == null || selector.identityHashCode != identityHashCode){
            selectors.put(key, new ConsistentHashSelector(invokers, identityHashCode));
            selector = (ConsistentHashSelector) selectors.get(key);
        }
        return selector.select(requestPacket);
    }
    //内部类
    public static class ConsistentHashSelector{
        //保存所有的虚拟节点，使用TreeMap
        private final TreeMap<Long, Invoker> virtualInvokers;
        //为每个服务节点创建160个虚拟节点
        private final int replicaNumber = 160;
        //服务列表的hashcode
        private final int identityHashCode;

        ConsistentHashSelector(List<Invoker> invokers, int identityHashCode){
            this.virtualInvokers = new TreeMap<>();
//            this.replicaNumber = 160;
            this.identityHashCode = identityHashCode;

            for(Invoker invoker : invokers){
                String address = invoker.getAddress();
                for(int i = 0; i < replicaNumber / 4; i++){
                    byte[] digest = md5(address + i);
                    for(int h = 0; h < 4; h ++){
                        long m = hash(digest, h);
                        virtualInvokers.put(m, invoker);
                    }
                }
            }
        }
        //根据传入参数的第一个进行hash，选择服务节点
        public Invoker select(ServiceRequestPacket requestPacket){
            Object[] args = requestPacket.getArgs();
            String key = args[0].toString();
            //先进行md5，保证分布足够分散
            byte[] digest = md5(key);
            //根据hash值进行选择
            return selectFromKey(hash(digest, 0));
        }
        private Invoker selectFromKey(long hash){
            Map.Entry<Long, Invoker> entry = virtualInvokers.tailMap(hash, true).firstEntry();
            if(entry == null){
                entry = virtualInvokers.firstEntry();
            }
            return entry.getValue();
        }

        private long hash(byte[] digest, int number) {
            return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                    | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                    | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                    | (digest[0 + number * 4] & 0xFF))
                    & 0xFFFFFFFFL;
        }
        private byte[] md5(String string){
            MessageDigest md5;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("MD5 not supported", e);
            }
            md5.reset();
            byte[] key = string.getBytes(StandardCharsets.UTF_8);
            md5.update(key);
            return md5.digest();

        }
    }
}
