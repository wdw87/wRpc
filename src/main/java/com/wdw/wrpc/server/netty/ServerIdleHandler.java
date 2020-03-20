package com.wdw.wrpc.server.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class ServerIdleHandler extends ChannelDuplexHandler {

    private static ServerIdleHandler serverIdleHandler;

    public static ServerIdleHandler getInstance(){
        if(serverIdleHandler == null){
            serverIdleHandler = new ServerIdleHandler();
        }
        return serverIdleHandler;
    }

    private ServerIdleHandler(){};

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            log.warn("客户端连接超时，关闭连接: " + ctx.channel().remoteAddress());
            ctx.channel().close();
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
