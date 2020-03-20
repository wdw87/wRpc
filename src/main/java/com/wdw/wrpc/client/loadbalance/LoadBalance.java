package com.wdw.wrpc.client.loadbalance;

import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Random;

@NoArgsConstructor
public class LoadBalance {

    public static String getAddress(List<String> serverList){
        if(serverList.isEmpty()){
            throw  new RuntimeException("没有可用的服务");
        }

        return random(serverList);

    }

    private static String random(List<String> serverList){
        return serverList.get(new Random().nextInt(serverList.size()));
    }

}
