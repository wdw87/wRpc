package com.wdw.wrpc.server.dispatcher;

import com.wdw.wrpc.common.protocal.ServiceRequestPacket;
import com.wdw.wrpc.common.protocal.ServiceResponsePacket;
import com.wdw.wrpc.common.util.SpringUtil;
import com.wdw.wrpc.config.ServiceConfig;
import com.wdw.wrpc.server.netty.ServiceInvoker;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

@Slf4j
@AllArgsConstructor
public class ChannelEventRunnable implements Runnable{
    private Channel channel;
    private ServiceRequestPacket serviceRequest;
    @Override
    public void run() {
        try {
            ServiceResponsePacket responsePacket = new ServiceResponsePacket();

            //设置相应id
            responsePacket.setRequestId(serviceRequest.getId());

            ApplicationContext context = SpringUtil.getApplicationContext();
            Map<String, ServiceConfig> map = context.getBeansOfType(ServiceConfig.class);

            Set<String> keySet = map.keySet();

            ServiceConfig serviceConfig = null;
            for (String key : keySet) {
                if (map.get(key).getName().equals(serviceRequest.getClassName())) {
                    serviceConfig = map.get(key);
                    break;
                }
            }

            if (serviceConfig == null) {
                log.info("No such service : " + serviceRequest);
                responsePacket.setCode(1);
                responsePacket.setMessage("No such service");
            } else {
                //获取服务的实现类
                Object object = context.getBean(serviceConfig.getRef());
                Method method = object.getClass().getMethod(serviceRequest.getMethodName(), serviceRequest.getParameterTypes());
                ServiceInvoker serviceInvoker = ServiceInvoker.INSTANCE;
                Object result = serviceInvoker.invoke(object, method, serviceRequest);
                log.info("service id : " + serviceRequest.getId());
                log.info("Service invoked : " + serviceRequest);
                responsePacket.setCode(0);
                responsePacket.setData(result);
            }

            channel.writeAndFlush(responsePacket);
        } catch (NoSuchMethodException e) {
            log.error("dispatcher thread error," + e.getMessage());
            e.printStackTrace();
        }
    }
}
