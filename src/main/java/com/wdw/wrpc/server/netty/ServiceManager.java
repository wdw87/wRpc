package com.wdw.wrpc.server.netty;

import com.wdw.wrpc.common.protocal.ServiceDescriptor;
import com.wdw.wrpc.common.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServiceManager {

    public static final ServiceManager INSTANCE = new ServiceManager();

    private Map<ServiceDescriptor, ServiceInstance> map = new ConcurrentHashMap<>();

    public <T> void register(Class<T> clazz, T instance){
        Method[] methods = ReflectionUtil.getPublicMethod(clazz);

        for(Method m : methods){
            ServiceDescriptor serviceDescriptor = ServiceDescriptor.from(clazz, m);
            ServiceInstance serviceInstance = new ServiceInstance(instance, m);

            map.put(serviceDescriptor, serviceInstance);
            log.info("register service {} {}" ,serviceDescriptor.getClazz(), serviceInstance.getMethod());
        }

    }

    public ServiceInstance lookup(ServiceDescriptor serviceDescriptor){
        return map.get(serviceDescriptor);
    }
}
