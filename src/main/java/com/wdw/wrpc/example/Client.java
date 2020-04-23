package com.wdw.wrpc.example;


import com.wdw.wrpc.client.proxy.ProxyFactory;
import com.wdw.wrpc.client.proxy.impl.JDKProxyFactory;
import com.wdw.wrpc.common.util.SpringUtil;
import com.wdw.wrpc.service.CalculateInterFace;
import com.wdw.wrpc.service.MultiplyInterface;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("consumer.xml");
        applicationContext.start();
//        ProxyFactory proxyFactory = new JDKProxyFactory();
//        CalculateInterFace bean = (CalculateInterFace)proxyFactory.getProxy(CalculateInterFace.class);
        CalculateInterFace bean = (CalculateInterFace)SpringUtil.getApplicationContext().getBean("calculate");
        System.out.println(bean.add(1,2));
        System.out.println(bean.dec(10,2));

        MultiplyInterface bean1 = (MultiplyInterface)SpringUtil.getApplicationContext().getBean("multiply");
        System.out.println(bean1.multiply(3.5,5.5));

        System.out.println(bean.dec(11,12));
    }
}
