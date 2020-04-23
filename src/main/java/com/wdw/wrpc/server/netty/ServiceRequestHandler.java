package com.wdw.wrpc.server.netty;

import com.alibaba.fastjson.JSON;
import com.wdw.wrpc.config.ServiceConfig;
import com.wdw.wrpc.common.protocal.ServiceRequestPacket;
import com.wdw.wrpc.common.protocal.ServiceResponsePacket;
import com.wdw.wrpc.common.util.SpringUtil;
import com.wdw.wrpc.server.dispatcher.ChannelEventRunnable;
import com.wdw.wrpc.server.dispatcher.Dispatcher;
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

    private Dispatcher dispatcher = Dispatcher.getInstance();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端建立了连接");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ServiceRequestPacket serviceRequest) throws Exception {
        /**
         * 将事件分发到线程池上执行
         */
        dispatcher.execute(new ChannelEventRunnable(channelHandlerContext.channel(), serviceRequest));

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
