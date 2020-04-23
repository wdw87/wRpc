package com.wdw.wrpc.client.netty;



import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {
    private static final ConcurrentHashMap<String , NettyClient> clientMap = new ConcurrentHashMap<>();
    private static ClientManager INSTANCE = null;
    public static ClientManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ClientManager();
        }
        return INSTANCE;
    }
    private ClientManager(){
    }

    public NettyClient getClient(String address){
        if(!clientMap.containsKey(address) || clientMap.get(address) == null){
            String[] adds = address.split(":");
            String ip = adds[0];
            int port = Integer.parseInt(adds[1]);
            NettyClient newClient = new NettyClient(ip, port);
            clientMap.put(address, newClient);
        }
        return clientMap.get(address);
    }
}
