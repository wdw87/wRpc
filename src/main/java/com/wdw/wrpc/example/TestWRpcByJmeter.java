package com.wdw.wrpc.example;

import com.wdw.wrpc.common.util.SpringUtil;
import com.wdw.wrpc.service.CalculateInterFace;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestWRpcByJmeter extends AbstractJavaSamplerClient {
    private static CalculateInterFace bean = null;

    @Override
    public void setupTest(JavaSamplerContext context) {
        System.out.println("-----Init-----");
        ApplicationContext sprintContext = new ClassPathXmlApplicationContext("consumer.xml");
        bean = (CalculateInterFace)SpringUtil.getApplicationContext().getBean("calculate");
        System.out.println("-----Start-----");
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        System.out.println("-----Complete-----");
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult result = new SampleResult();
        result.sampleStart();

        try {
            long begin = System.currentTimeMillis();

            int res = bean.add(1,2);
            long cost = System.currentTimeMillis() - begin;
            System.out.println("result: " + res + ", cost: " + cost + " ms");

            if(res != 3){
                result.setSuccessful(false);
                return result;
            }
        } catch (Exception e) {
            result.setSuccessful(false);
            e.printStackTrace();
        } finally {
            result.sampleEnd();
        }

        return result;
    }

    public static void main(String[] args) {
        JavaSamplerContext javaSamplerContext = new JavaSamplerContext(new Arguments());

        TestWRpcByJmeter testWRpcByJmeter = new TestWRpcByJmeter();
        testWRpcByJmeter.setupTest(javaSamplerContext);
        testWRpcByJmeter.runTest(javaSamplerContext);
        testWRpcByJmeter.teardownTest(javaSamplerContext);
    }

}
