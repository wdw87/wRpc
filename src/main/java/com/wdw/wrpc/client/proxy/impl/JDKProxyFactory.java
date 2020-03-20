package com.wdw.wrpc.client.proxy.impl;


import com.wdw.wrpc.client.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

public class JDKProxyFactory implements ProxyFactory {
    @Override
    public Object getProxy(Class<?> clazz) {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, new InvokerInvocationHandler(clazz));
    }
}
