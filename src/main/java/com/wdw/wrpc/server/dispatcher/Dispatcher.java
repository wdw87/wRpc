package com.wdw.wrpc.server.dispatcher;

import com.wdw.wrpc.common.protocal.ServiceRequestPacket;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Dispatcher {
    /**
     * Holder模式
     * 静态成员式的内部类，该内部类的实例与外部类的实例没有绑定关系
     * 而且只有被调用到才会装载，从而实现了延迟加载
     * 懒加载，线程安全
     */
    private static class SingletonHolder{
        private static Dispatcher instance = new Dispatcher();
    }
    private Dispatcher(){}
    public static Dispatcher getInstance(){
        return SingletonHolder.instance;
    }

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void execute(ChannelEventRunnable channelEventRunnable){
        executorService.execute(channelEventRunnable);
    }

}
