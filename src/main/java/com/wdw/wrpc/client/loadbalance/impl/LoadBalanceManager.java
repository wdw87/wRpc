package com.wdw.wrpc.client.loadbalance.impl;

import com.wdw.wrpc.client.loadbalance.LoadBalance;
import com.wdw.wrpc.client.registry.Invoker;
import com.wdw.wrpc.common.protocal.ServiceRequestPacket;
import com.wdw.wrpc.common.util.ReflectionUtil;
import lombok.Data;

import java.util.List;

public class LoadBalanceManager implements LoadBalance {
    public static String currLoadBalance = "random";
    private Class<? extends AbstractLoadBalance> loadBalanceClass = RandomLoadBalance.class;

    private static LoadBalanceManager INSTANCE = null;
    private LoadBalanceManager(){}
    public static LoadBalanceManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new LoadBalanceManager();
        }
        return INSTANCE;
    }

    private AbstractLoadBalance loadBalance = null;

    @Override
    public Invoker select(List<Invoker> invokers, ServiceRequestPacket requestPacket) {
        if(loadBalance == null){
            loadBalance = ReflectionUtil.newInstance(loadBalanceClass);
            currLoadBalance = loadBalance.getName();
        }
        return loadBalance.select(invokers, requestPacket);
    }


    public void setLoadBalanceClass(Class<? extends AbstractLoadBalance> loadBalanceClass) {
        this.loadBalanceClass = loadBalanceClass;
    }
}
