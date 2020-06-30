package com.wdw.wrpc.server.dispatcher;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory  implements ThreadFactory {
    private String workerThreadName ;

    private static final AtomicInteger threadId = new AtomicInteger(1);

    public DefaultThreadFactory(String workerThreadName){
        this.workerThreadName = workerThreadName;
    }
    @Override
    public Thread newThread(Runnable r) {

        Thread t = new Thread(r, workerThreadName + "-" + threadId.getAndIncrement());
        return t;
    }
}
