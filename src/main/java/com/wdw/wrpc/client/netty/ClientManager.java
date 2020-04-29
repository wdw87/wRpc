package com.wdw.wrpc.client.netty;



import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程，单例模式要用线程安全的方式
 * 加锁
 */
@Slf4j
public class ClientManager {
    private static final ConcurrentHashMap<String , NettyClient> clientMap = new ConcurrentHashMap<>();
    private static class SingletonHolder{
        private static final ClientManager INSTANCE = new ClientManager();
    }
    private final ReentrantLock lock = new ReentrantLock();
    public static ClientManager getInstance(){
        return SingletonHolder.INSTANCE;
    }
    private ClientManager(){
    }

    public NettyClient getClient(String address){
        lock.lock();
        if(!clientMap.containsKey(address) || clientMap.get(address) == null){
            String[] adds = address.split(":");
            String ip = adds[0];
            int port = Integer.parseInt(adds[1]);
            log.info("A new netty client created [" + address + "]");
            NettyClient newClient = new NettyClient(ip, port);
            clientMap.put(address, newClient);
        }
        lock.unlock();
        return clientMap.get(address);
    }
}
