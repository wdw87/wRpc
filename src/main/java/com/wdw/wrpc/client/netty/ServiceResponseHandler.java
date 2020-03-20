package com.wdw.wrpc.client.netty;


import com.wdw.wrpc.common.protocal.ServiceRequestPacket;
import com.wdw.wrpc.common.protocal.ServiceResponsePacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

@Slf4j
@NoArgsConstructor
public class ServiceResponseHandler extends SimpleChannelInboundHandler<ServiceResponsePacket> {


    private Map<String, SynchronousQueue<Object>> queueMap = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client connected.");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ServiceResponsePacket serviceResponsePacket) throws Exception {
        String id = serviceResponsePacket.getRequestId();
        SynchronousQueue<Object> queue = queueMap.get(id);

        if(queue != null){
            queue.put(serviceResponsePacket);
            queueMap.remove(id);
        }else{
            log.error("request id error !!!");
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client disconnected.");
        super.channelInactive(ctx);
    }

    public SynchronousQueue<Object> sendRequest(ServiceRequestPacket requestPacket, Channel channel){
        SynchronousQueue<Object> queue = new SynchronousQueue<>();
        queueMap.put(requestPacket.getId(), queue);
        channel.writeAndFlush(requestPacket);
        return queue;
    }
}
