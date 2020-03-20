package com.wdw.wrpc.config;

import lombok.Data;

@Data
public class RegistryConfig {
    private String id;
    private String registryAddress ;
    private String zkPath;
    private int sessionTimeout;
    private int connectionTimeout;


}
