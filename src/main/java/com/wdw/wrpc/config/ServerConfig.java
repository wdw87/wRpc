package com.wdw.wrpc.config;

import com.wdw.wrpc.server.netty.NettyServer;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;

@Data
public class ServerConfig implements InitializingBean {
    private String id;
    private String ip;
    private int port;
    private int weight;

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(()->{
            NettyServer nettyServer = new NettyServer(port, weight);
            nettyServer.start();
        }).start();
    }
}
