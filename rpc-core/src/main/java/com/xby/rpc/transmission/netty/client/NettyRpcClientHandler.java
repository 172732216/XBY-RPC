package com.xby.rpc.transmission.netty.client;

import com.xby.rpc.constant.RpcConstant;
import com.xby.rpc.dto.RpcResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String rpcResp) throws Exception {
        log.debug("收到服务端数据:{}", rpcResp);

        AttributeKey<String> key=AttributeKey.valueOf(RpcConstant.NETTY_RPC_KEY);
        ctx.channel().attr(key).set(rpcResp);
        ctx.channel().close();
    }
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("服务端发生异常",cause);

    }
}
