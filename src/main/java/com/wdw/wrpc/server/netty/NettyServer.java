package com.wdw.wrpc.server.netty;

import com.wdw.wrpc.common.protocal.codec.Decoder;
import com.wdw.wrpc.common.protocal.codec.Encoder;
import com.wdw.wrpc.common.protocal.codec.Spliter;
import com.wdw.wrpc.server.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@Slf4j
@NoArgsConstructor
public class NettyServer {
    private int port;


    //服务交由spring管理了,在bean初始化完毕后直接在spring中注册
//    private ServiceManager serviceManager;

//    private ServiceRegistry serviceRegistry ServiceRegistry.getInstance();

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private ServiceRequestHandler serviceRequestHandler = ServiceRequestHandler.INSTANCE;

    public NettyServer(int port) {

        this.port = port;

    }

    public void start(){

        log.info("服务端启动中...");
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)   //启用tcp协议层面的keep-live
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new Spliter());
                            socketChannel.pipeline().addLast(new Decoder());
                            socketChannel.pipeline().addLast(new IdleStateHandler(0,0,90, TimeUnit.SECONDS));
                            socketChannel.pipeline().addLast(HeartbeatHandler.getInstance());
                            socketChannel.pipeline().addLast(serviceRequestHandler);
                            socketChannel.pipeline().addLast(ServerIdleHandler.getInstance());
                            socketChannel.pipeline().addLast(new Encoder());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("server boot successful, bind port [{}]", port);
                    } else {
                        log.warn("server boot failed.....");
                    }
                }
            });
            //等待服务关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("RPC服务端启动异常，监听【" + port + "】端口", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }


    //服务交由spring管理，该方法废弃
    //    public <T> void register(Class<T> clazz, T object) {
//        serviceManager.register(clazz, object);
//        serviceRegistry.register(clazz.getName());
//    }

    public void close() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

}
