package com.xby.rpc.transmission.netty.server;

import com.xby.rpc.config.RpcServiceConfig;
import com.xby.rpc.constant.RpcConstant;
import com.xby.rpc.factory.SingletonFactory;
import com.xby.rpc.provider.ServiceProvider;
import com.xby.rpc.provider.impl.ZkServiceProvider;
import com.xby.rpc.transmission.RpcServer;
import com.xby.rpc.transmission.netty.client.NettyRpcClientHandler;
import com.xby.rpc.transmission.netty.codec.NettyRpcDecoder;
import com.xby.rpc.transmission.netty.codec.NettyRpcEncoder;
import com.xby.rpc.util.ShutdownHookUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyRpcServer implements RpcServer {
    private final ServiceProvider serviceProvider;
    private final int port;

    public NettyRpcServer(){
        this(RpcConstant.SERVER_PORT);
    }

    public NettyRpcServer(int port){
        this(SingletonFactory.getInstance(ZkServiceProvider.class), port);
    }

    public NettyRpcServer(ServiceProvider serviceProvider){
        this(serviceProvider,RpcConstant.SERVER_PORT);
    }

    public NettyRpcServer(ServiceProvider serviceProvider, int port) {
        this.serviceProvider = serviceProvider;
        this.port = port;
    }

    @Override
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new IdleStateHandler(30,0,0, TimeUnit.SECONDS));
                            channel.pipeline().addLast(new NettyRpcDecoder());
                            channel.pipeline().addLast(new NettyRpcEncoder());
                            channel.pipeline().addLast(new NettyRpcServerHandler(serviceProvider));
                        }
                    });

            ShutdownHookUtils.clearAll();
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            log.info("netty rpc server已启动,端口：{}",port);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("服务端异常",e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void publishService(RpcServiceConfig config) {
        serviceProvider.publishService(config);
    }
}


