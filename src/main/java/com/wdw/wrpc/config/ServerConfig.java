package com.wdw.wrpc.config;

import com.wdw.wrpc.server.netty.NettyServer;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;

@Data
public class ServerConfig implements InitializingBean {
    private String id;
    private int port;

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(()->{
            NettyServer nettyServer = new NettyServer(port);
            nettyServer.start();
        }).start();
    }
}
