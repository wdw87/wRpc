package com.wdw.wrpc.client.proxy.impl;


import com.wdw.wrpc.client.loadbalance.LoadBalance;
import com.wdw.wrpc.client.netty.ClientManager;
import com.wdw.wrpc.client.netty.NettyClient;
import com.wdw.wrpc.client.registry.ServiceDiscovery;
import com.wdw.wrpc.common.protocal.ServiceResponsePacket;
import com.wdw.wrpc.common.protocal.ServiceRequestPacket;
import com.wdw.wrpc.common.util.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class InvokerInterceptor implements MethodInterceptor {

    private Class<?> clazz;

    private ServiceDiscovery serviceDiscovery = ServiceDiscovery.getInstance();

//    private NettyClient client = null;

    public InvokerInterceptor(Class<?> clazz){
        this.clazz = clazz;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        ServiceRequestPacket requestPacket = new ServiceRequestPacket();

        requestPacket.setId(IDUtil.getUUID());

        requestPacket.setClassName(clazz.getName());

        requestPacket.setMethodName(method.getName());

        requestPacket.setArgs(objects);

        requestPacket.setParameterTypes(method.getParameterTypes());

        requestPacket.setReturnType(method.getReturnType());

//        requestPacket.setServiceDescriptor(ServiceDescriptor.from(clazz, method));
//
//        requestPacket.setArgs(objects);

        //负载均衡
        List<String> list = serviceDiscovery.getServerList(clazz.getName());
        String serverAddress = LoadBalance.getAddress(list);

        NettyClient client = ClientManager.getInstance().getClient(serverAddress);

        ServiceResponsePacket responsePacket = (ServiceResponsePacket)client.send(requestPacket);

        if(responsePacket.getCode() == 0){
            return responsePacket.getData();
        }else{
            log.error(responsePacket.getMessage());
            return null;
        }

    }
}
