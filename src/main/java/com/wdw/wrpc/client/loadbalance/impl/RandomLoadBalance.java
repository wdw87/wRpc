package com.wdw.wrpc.client.loadbalance.impl;

import com.wdw.wrpc.client.registry.Invoker;
import com.wdw.wrpc.common.protocal.ServiceRequestPacket;

import java.util.List;
import java.util.Random;

/**
 * 权重随机
 */
public class RandomLoadBalance extends AbstractLoadBalance{
    public static final String NAME = "random";
    private final Random random = new Random();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected Invoker doSelect(List<Invoker> invokers, ServiceRequestPacket requestPacket) {
        int length = invokers.size();
        int totalWeight = 0;
        boolean sameWeight = true;
        //计算权重和，并看权重是否相同
        for(int i = 0; i < length; i++){
            int weight = invokers.get(i).getWeight();
            totalWeight += weight;
            if(sameWeight && i > 0 && weight != invokers.get(i - 1).getWeight()){
                sameWeight = false;
            }
        }
        //如果权重相同，直接进行随机选择
        //否则根据权重进行选择
        if(totalWeight > 0 && !sameWeight){
            int offset = random.nextInt(totalWeight);
            //寻找选择结果
            for(int i = 0; i < length; i++){
                offset -= invokers.get(i).getWeight();
                if(offset < 0){
                    return invokers.get(i);
                }
            }
        }

        return invokers.get(random.nextInt(length));
    }
}
