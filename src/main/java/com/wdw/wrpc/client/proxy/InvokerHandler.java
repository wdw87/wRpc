package com.wdw.wrpc.client.proxy;

import com.wdw.wrpc.client.loadbalance.impl.LoadBalanceManager;
import com.wdw.wrpc.client.netty.ClientManager;
import com.wdw.wrpc.client.netty.NettyClient;
import com.wdw.wrpc.client.registry.Invoker;
import com.wdw.wrpc.client.registry.ServiceDiscovery;
import com.wdw.wrpc.common.protocal.ServiceRequestPacket;
import com.wdw.wrpc.common.protocal.ServiceResponsePacket;
import com.wdw.wrpc.common.util.IDUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class InvokerHandler {
    private Class<?> clazz;

    private ServiceDiscovery serviceDiscovery = ServiceDiscovery.getInstance();

    public InvokerHandler(Class<?> clazz){
        this.clazz = clazz;
    }

    public Object doInvoke(Method method, Object[] args) throws InterruptedException {
        ServiceRequestPacket requestPacket = new ServiceRequestPacket();

        requestPacket.setId(IDUtil.getUUID());

        requestPacket.setClassName(clazz.getName());

        requestPacket.setMethodName(method.getName());

        requestPacket.setArgs(args);

        requestPacket.setParameterTypes(method.getParameterTypes());

        requestPacket.setReturnType(method.getReturnType());

//        requestPacket.setServiceDescriptor(ServiceDescriptor.from(clazz, method));
//
//        requestPacket.setArgs(objects);

        //负载均衡
        List<Invoker> invokers = serviceDiscovery.getServerList(clazz.getName());
        Invoker invoker = LoadBalanceManager.getInstance().select(invokers, requestPacket);
        if(invoker == null){
            log.warn("service not fount : [{}]", requestPacket.getClassName() + "." + requestPacket.getMethodName());
            return null;
        }
        NettyClient client = ClientManager.getInstance().getClient(invoker.getAddress());

        ServiceResponsePacket responsePacket = (ServiceResponsePacket)client.send(requestPacket);

        if(responsePacket.getCode() == 0) {
            log.info("invoke success, remote:{} , load balance: {}",invoker.getAddress(),LoadBalanceManager.currLoadBalance);
            return responsePacket.getData();
        }else{
            log.error("invoke error ! code: " + responsePacket.getCode() + ". error info: " + responsePacket.getMessage());
            return null;
        }

    }
}
