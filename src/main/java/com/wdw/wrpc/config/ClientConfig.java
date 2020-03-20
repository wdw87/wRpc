package com.wdw.wrpc.config;

import com.wdw.wrpc.client.proxy.impl.CGLIBProxyFactory;
import com.wdw.wrpc.client.proxy.impl.JDKProxyFactory;
import com.wdw.wrpc.client.proxy.impl.ProxyManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

@Data
@Slf4j
public class ClientConfig implements InitializingBean {
    private String id;
    private String loadBalance;
    private String proxy;

    @Override
    public void afterPropertiesSet() throws Exception {
        ProxyManager proxyManager = ProxyManager.getInstance();
        if(proxy .equals("cglib")){
            proxyManager.setProxyClass(CGLIBProxyFactory.class);
        }else if(proxy.equals("jdk")){
            proxyManager.setProxyClass(JDKProxyFactory.class);
        }else{
            log.error("Proxy config err, no such proxy {}",proxy);
        }
    }
}
