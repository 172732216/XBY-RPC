package com.xby.rpc.transmission.netty.codec;

import com.xby.rpc.compress.Compress;
import com.xby.rpc.constant.RpcConstant;
import com.xby.rpc.dto.RpcMeg;
import com.xby.rpc.factory.SingletonFactory;
import com.xby.rpc.serialize.KryoSerializer;
import com.xby.rpc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class NettyRpcEncoder extends MessageToByteEncoder<RpcMeg> {
    private static final AtomicInteger ID_GEN=new AtomicInteger(0);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMeg rpcMsg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(RpcConstant.RPC_MAGIC_CODE);
        byteBuf.writeByte(rpcMsg.getVersionType().getCode());

        byteBuf.writerIndex(byteBuf.writerIndex()+1);

        byteBuf.writeByte(rpcMsg.getMsgType().getCode());
        byteBuf.writeByte(rpcMsg.getSerializeType().getCode());
        byteBuf.writeByte(rpcMsg.getCompressType().getCode());
        byteBuf.writeInt(ID_GEN.getAndIncrement());

        int msgLen=RpcConstant.REQ_HEAD_LEN;
        if(!rpcMsg.getMsgType().isHeartbeat()&&!Objects.isNull(rpcMsg.getData())) {
            byte[] data=data2Bytes(rpcMsg);
            byteBuf.writeBytes(data);
            msgLen+=data.length;
        }
        int curIdx = byteBuf.writerIndex();
        byteBuf.writerIndex(curIdx-msgLen+RpcConstant.RPC_MAGIC_CODE.length+1);
        byteBuf.writeInt(msgLen);
        byteBuf.writerIndex(curIdx);
    }

    private byte[] data2Bytes(RpcMeg rpcMsg) {

        Serializer serializer = SingletonFactory.getInstance(KryoSerializer.class);
        Compress compress=SingletonFactory.getInstance(Compress.class);
        byte[] data=serializer.serialize(rpcMsg.getData());
        return compress.compress(data);
    }
}
