package com.xby.rpc.transmission.netty.client;

import com.xby.rpc.constant.RpcConstant;
import com.xby.rpc.dto.RpcMsg;
import com.xby.rpc.dto.RpcResp;
import com.xby.rpc.enums.CompressType;
import com.xby.rpc.enums.MsgType;
import com.xby.rpc.enums.SerializeType;
import com.xby.rpc.enums.VersionType;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<RpcMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMsg rpcMsg) throws Exception {
        if(rpcMsg.getMsgType().isHeartbeat()){
            log.debug("收到服务端心跳:{}",rpcMsg);
            return;
        }

        log.debug("收到服务端数据:{}",rpcMsg);

        RpcResp<?> rpcResp=(RpcResp<?>)rpcMsg.getData();

        UnprocessedRpcReq.complete(rpcResp);

    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        boolean isNeedHeartBeat=evt instanceof IdleStateEvent && ((IdleStateEvent)evt).state()== IdleState.WRITER_IDLE;
        if(!isNeedHeartBeat){
            super.userEventTriggered(ctx, evt);
            return;
        }
        RpcMsg rpcMsg = RpcMsg.builder()
                .version(VersionType.VERSION1)
                .serializeType(SerializeType.KRYO)
                .compressType(CompressType.GZIP)
                .msgType(MsgType.HEARTBEAT_REQ)
                .build();
        log.debug("客户端发送心跳，{}",rpcMsg);
        ctx.writeAndFlush(rpcMsg).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("服务端发生异常",cause);
        ctx.close();
    }
}
