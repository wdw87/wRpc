package com.wdw.wrpc.config;

import com.wdw.wrpc.server.netty.ServiceManager;
import com.wdw.wrpc.server.registry.ServiceRegistry;
import com.wdw.wrpc.common.util.SpringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Data
@Slf4j
public class ServiceConfig implements ApplicationContextAware, InitializingBean {

    private String id;
    private String name;
//    private String impl;
    private String ref;

    private ApplicationContext applicationContext;
//服务交由spring管理
//    private ServiceManager serviceManager;
    private ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!applicationContext.containsBean("server")) {
            log.info("没有配置server，不能发布到注册中心");
            return;
        }
        if (!applicationContext.containsBean("registry")) {
            log.info("registry，不能发布到注册中心");
            return;
        }
        ApplicationContext context = SpringUtil.getApplicationContext();
        ServerConfig serverConfig = (ServerConfig)context.getBean("server");
        RegistryConfig registryConfig = (RegistryConfig)context.getBean("registry");
        //连接zookeeper
        serviceRegistry.connectServer(serverConfig,registryConfig);
        //将服务发布到注册中心
        serviceRegistry.register(name);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
