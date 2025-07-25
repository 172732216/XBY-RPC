package com.xby.rpc.transmission.netty.server;

import com.xby.rpc.dto.RpcMsg;
import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.dto.RpcResp;
import com.xby.rpc.enums.CompressType;
import com.xby.rpc.enums.MsgType;
import com.xby.rpc.enums.SerializeType;
import com.xby.rpc.enums.VersionType;
import com.xby.rpc.factory.SingletonFactory;
import com.xby.rpc.handle.RpcReqHander;
import com.xby.rpc.provider.ServiceProvider;
import com.xby.rpc.transmission.RpcServer;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcMsg> {
    private final RpcReqHander rpcReqHander;

    public NettyRpcServerHandler(ServiceProvider serviceProvider) {
        this.rpcReqHander = new RpcReqHander(serviceProvider);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMsg rpcMsg) throws Exception {
        log.debug("接收到客户端请求:{}", rpcMsg);

        MsgType msgType;
        Object data;
        if(rpcMsg.getMsgType().isHeartbeat()){
            msgType=MsgType.HEARTBEAT_RESP;
            data=null;
        }else{
            msgType=MsgType.RPC_RESP;
            RpcReq rpcReq=(RpcReq)rpcMsg.getData();
            data=handleRpcReq(rpcReq);
        }

        RpcMsg msg = RpcMsg.builder()
                .reqId(rpcMsg.getReqId())
                .version(VersionType.VERSION1)
                .serializeType(SerializeType.KRYO)
                .compressType(CompressType.GZIP)
                .msgType(msgType)
                .data(data)
                .build();


        ctx.channel()
                .writeAndFlush(msg)
                .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        boolean isNeedClose=evt instanceof IdleStateEvent && ((IdleStateEvent)evt).state()== IdleState.READER_IDLE;
        if(!isNeedClose){
            super.userEventTriggered(ctx, evt);
            return;
        }

        log.debug("服务端长时间没有收到客户端的心跳，关闭channel，addr:{}", ctx.channel().remoteAddress());
        ctx.channel().close();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("服务端异常",cause);
        ctx.close();
    }
    private RpcResp<?> handleRpcReq(RpcReq rpcReq){
        try{
            Object object=rpcReqHander.invoke(rpcReq);
            return RpcResp.success(rpcReq.getReqId(),object);
        }catch(Exception e){
            log.info("调用失败");
            return RpcResp.fail(rpcReq.getReqId(), e.getMessage());
        }
    }

}

