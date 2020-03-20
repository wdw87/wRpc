package com.wdw.wrpc.client.proxy;

public interface ProxyFactory {
    Object getProxy(Class<?> clazz);
}
