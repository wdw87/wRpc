package com.wdw.wrpc.client.netty;


import com.wdw.wrpc.common.protocal.HeartbeatPacket;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartbeatHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            log.debug("heart beat...");
            ctx.channel().writeAndFlush(new HeartbeatPacket());
        }else{

            super.userEventTriggered(ctx, evt);
        }
    }
}
