package com.wdw.wrpc.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class NameSpaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("application", new SimpleBeanDefinitionParser(ApplicationConfig.class));
        registerBeanDefinitionParser("registry", new SimpleBeanDefinitionParser(RegistryConfig.class));
        registerBeanDefinitionParser("server", new SimpleBeanDefinitionParser(ServerConfig.class));
        registerBeanDefinitionParser("service", new SimpleBeanDefinitionParser(ServiceConfig.class));
        registerBeanDefinitionParser("client", new SimpleBeanDefinitionParser(ClientConfig.class));
        registerBeanDefinitionParser("reference", new SimpleBeanDefinitionParser(ReferenceConfig.class));

    }
}
