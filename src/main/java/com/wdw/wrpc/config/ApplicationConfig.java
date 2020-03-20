package com.wdw.wrpc.config;

import com.wdw.wrpc.common.util.IPUtil;
import com.wdw.wrpc.common.util.SpringUtil;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Data
public class ApplicationConfig implements ApplicationContextAware, InitializingBean {
    private String id;
    private String name;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.setApplicationContext(applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RpcContext.setApplicationName(name);
        RpcContext.setLocalIp(IPUtil.getIpAddress());
    }
}
