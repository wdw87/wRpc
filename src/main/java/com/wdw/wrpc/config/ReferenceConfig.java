package com.wdw.wrpc.config;

import com.wdw.wrpc.client.proxy.ProxyFactory;
import com.wdw.wrpc.client.proxy.impl.JDKProxyFactory;
import com.wdw.wrpc.client.proxy.impl.ProxyManager;
import com.wdw.wrpc.client.registry.ServiceDiscovery;
import com.wdw.wrpc.service.CalculateInterFace;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Slf4j
@Data
public class ReferenceConfig implements ApplicationContextAware, InitializingBean, FactoryBean {
    private String id;
    private String name;
    private int timeout;
    private ApplicationContext applicationContext;

    private ServiceDiscovery serviceDiscovery;


    @Override
    public void afterPropertiesSet() throws Exception {
        serviceDiscovery = ServiceDiscovery.getInstance();
        serviceDiscovery.connectServer();
        serviceDiscovery.discovery(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }


    @Override
    public Object getObject() throws Exception {
        ProxyManager proxyManager = ProxyManager.getInstance();
        Class<?> clazz = getObjectType();
        return proxyManager.getProxy(clazz);
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            log.error("没有对应的服务", e);
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
