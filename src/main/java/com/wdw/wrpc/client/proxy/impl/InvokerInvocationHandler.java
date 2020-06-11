package com.wdw.wrpc.client.proxy.impl;


import com.wdw.wrpc.client.proxy.InvokerHandler;
import com.wdw.wrpc.client.registry.ServiceDiscovery;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class InvokerInvocationHandler extends InvokerHandler implements InvocationHandler {

    public InvokerInvocationHandler(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return doInvoke(method, args);
    }
}
