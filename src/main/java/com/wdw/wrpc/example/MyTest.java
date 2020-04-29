package com.wdw.wrpc.example;

import com.wdw.wrpc.common.util.SpringUtil;
import com.wdw.wrpc.service.CalculateInterFace;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;

public class MyTest {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("consumer.xml");
        long start = System.currentTimeMillis();
        int threadCount = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for(int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                CalculateInterFace bean = (CalculateInterFace) SpringUtil.getApplicationContext().getBean("calculate");
                System.out.println("result : " + bean.add(1,2));
                countDownLatch.countDown();
            }, "rpc-thread-" + i).start();
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("thread count: " + threadCount + " , cost: " + (end - start) + " ms");
    }
}
