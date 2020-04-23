package com.wdw.wrpc.client.proxy.impl;


import com.wdw.wrpc.client.loadbalance.LoadBalance;
import com.wdw.wrpc.client.netty.ClientManager;
import com.wdw.wrpc.client.netty.NettyClient;
import com.wdw.wrpc.client.registry.ServiceDiscovery;
import com.wdw.wrpc.common.protocal.ServiceRequestPacket;
import com.wdw.wrpc.common.protocal.ServiceResponsePacket;
import com.wdw.wrpc.common.util.IDUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

@Slf4j
@Data
public class InvokerInvocationHandler implements InvocationHandler {
    private Class<?> clazz;

//    private NettyClient client = null;
    private ServiceDiscovery serviceDiscovery = ServiceDiscovery.getInstance();

    public InvokerInvocationHandler(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        ServiceRequestPacket requestPacket = new ServiceRequestPacket();

        requestPacket.setId(IDUtil.getUUID());

        requestPacket.setClassName(clazz.getName());

        requestPacket.setMethodName(method.getName());

        requestPacket.setArgs(args);

        requestPacket.setParameterTypes(method.getParameterTypes());

//        requestPacket.setServiceDescriptor(ServiceDescriptor.from(clazz, method));
//
//        requestPacket.setArgs(args);

//        if(client == null) {
//            //负载均衡
//            String serverAddress = LoadBalance.getAddress(serviceDiscovery.getServerList(clazz.getName()));
//            String[] address = serverAddress.split(":");
//            client = new NettyClient(address[0], Integer.parseInt(address[1]));
//        }
        List<String> list = serviceDiscovery.getServerList(clazz.getName());
        String serverAddress = LoadBalance.getAddress(list);

        NettyClient client = ClientManager.getInstance().getClient(serverAddress);

        ServiceResponsePacket responsePacket = (ServiceResponsePacket)client.send(requestPacket);
        if(responsePacket.getCode() == 0) {
            return responsePacket.getData();
        }else{
            log.error("invoke error ! code: " + responsePacket.getCode() + ". error info: " + responsePacket.getMessage());
            return null;
        }
    }
}
