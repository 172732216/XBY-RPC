package com.xby.rpc.transmission.netty.codec;

import com.xby.rpc.compress.Compress;
import com.xby.rpc.compress.impl.GzipCompress;
import com.xby.rpc.constant.RpcConstant;
import com.xby.rpc.dto.RpcMsg;
import com.xby.rpc.factory.SingletonFactory;
import com.xby.rpc.serialize.KryoSerializer;
import com.xby.rpc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class NettyRpcEncoder extends MessageToByteEncoder<RpcMsg> {
    private static final AtomicInteger ID_GEN=new AtomicInteger(0);

    private byte[] data2Bytes(RpcMsg rpcMsg){
        Serializer serializer =SingletonFactory.getInstance(KryoSerializer.class);
        byte[] data=serializer.serialize(rpcMsg.getData());
        Compress compress=SingletonFactory.getInstance(GzipCompress.class);
        return compress.compress(data);

    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMsg rpcMsg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(RpcConstant.RPC_MAGIC_CODE);
        byteBuf.writeByte(rpcMsg.getVersion().getCode());

        // 往右挪动4位, 给报文长度腾出空间
        byteBuf.writerIndex(byteBuf.writerIndex() + 4);

        byteBuf.writeByte(rpcMsg.getMsgType().getCode());
        byteBuf.writeByte(rpcMsg.getSerializeType().getCode());
        byteBuf.writeByte(rpcMsg.getCompressType().getCode());
        byteBuf.writeInt(ID_GEN.getAndIncrement());

        int msgLen = RpcConstant.REQ_HEAD_LEN;
        if (!rpcMsg.getMsgType().isHeartbeat()
                && !Objects.isNull(rpcMsg.getData())) {
            byte[] data = data2Bytes(rpcMsg);
            byteBuf.writeBytes(data);
            msgLen += data.length;
        }

        int curIdx = byteBuf.writerIndex();
        byteBuf.writerIndex(curIdx - msgLen + RpcConstant.RPC_MAGIC_CODE.length + 1);
        byteBuf.writeInt(msgLen);
        byteBuf.writerIndex(curIdx);

    }


}

