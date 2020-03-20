package com.wdw.wrpc.client.proxy.impl;

import com.wdw.wrpc.client.proxy.ProxyFactory;
import com.wdw.wrpc.common.util.ReflectionUtil;
import lombok.Data;

@Data
public class ProxyManager implements ProxyFactory {

    private Class<? extends ProxyFactory> ProxyClass = JDKProxyFactory.class;

    private ProxyFactory proxyFactory = null;

    private ProxyManager(){}

    private static ProxyManager INSTANCE;

    public static ProxyManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ProxyManager();
        }
        return INSTANCE;
    }

    @Override
    public Object getProxy(Class<?> clazz) {
        if(proxyFactory == null){
            proxyFactory = ReflectionUtil.newInstance(ProxyClass);
        }
        return proxyFactory.getProxy(clazz);
    }
}
