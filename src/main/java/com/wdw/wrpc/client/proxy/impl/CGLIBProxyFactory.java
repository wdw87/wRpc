package com.wdw.wrpc.client.proxy.impl;


import com.wdw.wrpc.client.proxy.ProxyFactory;
import org.springframework.cglib.proxy.Enhancer;

public class CGLIBProxyFactory implements ProxyFactory {
    @Override
    public Object getProxy(Class<?> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new InvokerInterceptor(clazz));
        return enhancer.create();
    }
}
