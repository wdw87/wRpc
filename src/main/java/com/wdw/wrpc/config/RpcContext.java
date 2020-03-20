package com.wdw.wrpc.config;


public class RpcContext {
    private static String applicationName;
    private static String localIp;

    public static String getApplicationName() {
        return applicationName;
    }

    public static void setApplicationName(String applicationName) {
        RpcContext.applicationName = applicationName;
    }

    public static String getLocalIp() {
        return localIp;
    }

    public static void setLocalIp(String localIp) {
        RpcContext.localIp = localIp;
    }
}
