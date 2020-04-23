package com.wdw.wrpc.server.registry;

import com.wdw.wrpc.config.RegistryConfig;
import com.wdw.wrpc.config.RpcContext;
import com.wdw.wrpc.config.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

@Slf4j
public class ServiceRegistry {


    //    private static String registryAddress = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
    private String registryAddress = "123.57.175.207:2181";
    private String myAddress = "127.0.0.1:8000";
    private int weight = 100;
    private String zkPath = "/wrpc";
    private int sessionTimeout = 10000;
    private int connectionTimeout = 10000;
    private ZkClient zkClient;

    private static ServiceRegistry serviceRegistry = null;

    public static ServiceRegistry getInstance(){
        if(serviceRegistry == null){
            serviceRegistry = new ServiceRegistry();
        }
        return serviceRegistry;
    }

    private ServiceRegistry() {

    }

    public void setRegistryParams(ServerConfig serverConfig, RegistryConfig registryConfig){
        this.registryAddress = registryConfig.getRegistryAddress();
        this.myAddress = RpcContext.getLocalIp() + ":" + serverConfig.getPort();
        this.weight = serverConfig.getWeight();
        this.zkPath = registryConfig.getZkPath();
        this.sessionTimeout = registryConfig.getSessionTimeout();
        this.connectionTimeout = registryConfig.getConnectionTimeout();
    }

    public void connectServer(ServerConfig serverConfig, RegistryConfig registryConfig){
        setRegistryParams(serverConfig,registryConfig);
        connectServer();
    }

    public void connectServer() {
        try {
            if(zkClient == null) {
                zkClient = new ZkClient(registryAddress, sessionTimeout, connectionTimeout);
                log.info("zookeeper 已连接");
            }
        } catch (Exception e) {
            log.error("zookeeper 连接异常!!!, " + e.getMessage());
        }

    }

    public void register(String service) {
        if (zkClient != null) {
            if (!zkClient.exists(zkPath)) {
                addRootNode();
            }
            if (!zkClient.exists(zkPath + "/" + service)) {
                zkClient.createPersistent(zkPath + "/" + service + "/providers", true);
            }

            String path = zkPath + "/" + service + "/providers/" + myAddress;
            if(zkClient.exists(path)) {
                zkClient.delete(path);
            }
            zkClient.createEphemeral(path, weight);
            log.info("注册了服务：" + path);
        } else {
            log.warn("zookeeper客户端未初始化");
        }
    }

    private void addRootNode() {
        boolean exists = zkClient.exists(zkPath);
        if (!exists) {
            zkClient.createPersistent(zkPath);
            log.info("创建wrpc根节点 : " + zkPath);
        }
    }
}
