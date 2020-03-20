package com.wdw.wrpc.server.netty;

import com.wdw.wrpc.common.protocal.HeartbeatPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class HeartbeatHandler extends SimpleChannelInboundHandler<HeartbeatPacket> {
    private static HeartbeatHandler heartbeatHandler = null;
    public static HeartbeatHandler getInstance(){
        if(heartbeatHandler == null){
            heartbeatHandler = new HeartbeatHandler();
        }
        return heartbeatHandler;
    }
    private HeartbeatHandler(){};
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HeartbeatPacket heartbeatPacket) throws Exception {
        log.debug("Got ping from : " + channelHandlerContext.channel().remoteAddress());
    }
}
