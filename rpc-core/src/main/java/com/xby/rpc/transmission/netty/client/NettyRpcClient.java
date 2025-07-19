package com.xby.rpc.transmission.netty.client;

import com.xby.rpc.constant.RpcConstant;
import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.dto.RpcResp;
import com.xby.rpc.transmission.RpcClient;
import com.xby.rpc.transmission.netty.codec.NettyRpcDecoder;
import com.xby.rpc.transmission.netty.codec.NettyRpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyRpcClient implements RpcClient {
    private static final Bootstrap bootstrap;
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;

    static {
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, DEFAULT_CONNECT_TIMEOUT)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new NettyRpcDecoder());
                        channel.pipeline().addLast(new NettyRpcEncoder());
                        channel.pipeline().addLast(new NettyRpcClientHandler());
                    }
                });


    }
    @SneakyThrows
    @Override
    public RpcResp<?> sendReq(RpcReq req) {
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", RpcConstant.SERVER_PORT).sync();

        log.info("netty rpc client连接到xxx");

        Channel channel = channelFuture.channel();
        String interfaceName = req.getInterfaceName();
        channel.writeAndFlush(interfaceName).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

        channel.closeFuture().sync();

        AttributeKey<String>key = AttributeKey.valueOf(RpcConstant.NETTY_RPC_KEY);

        String s=channel.attr(key).get();
        System.out.println(s);
        return null;
    }
}
