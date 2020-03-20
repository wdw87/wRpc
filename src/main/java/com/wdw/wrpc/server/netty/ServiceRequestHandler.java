package com.wdw.wrpc.server.netty;

import com.alibaba.fastjson.JSON;
import com.wdw.wrpc.config.ServiceConfig;
import com.wdw.wrpc.common.protocal.ServiceRequestPacket;
import com.wdw.wrpc.common.protocal.ServiceResponsePacket;
import com.wdw.wrpc.common.util.SpringUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

@Slf4j
@NoArgsConstructor
@ChannelHandler.Sharable
public class ServiceRequestHandler extends SimpleChannelInboundHandler<ServiceRequestPacket> {

    public static final ServiceRequestHandler INSTANCE = new ServiceRequestHandler();

    private ServiceInvoker serviceInvoker = ServiceInvoker.INSTANCE;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端建立了连接");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ServiceRequestPacket serviceRequest) throws Exception {
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
            Object result = serviceInvoker.invoke(object, method, serviceRequest);
            log.info("service id : " + serviceRequest.getId());
            log.info("Service invoked : " + serviceRequest);
            responsePacket.setCode(0);
            responsePacket.setData(result);
        }

        channelHandlerContext.channel().writeAndFlush(responsePacket);

    }




    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开了连接");
        super.channelInactive(ctx);
    }
}
