package com.wdw.wrpc.example;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Server {
    public static void main(String[] args) throws InterruptedException {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("provider.xml");
        context.start();

    }
}
