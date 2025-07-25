package com.xby.client.utils;

import com.xby.rpc.factory.SingletonFactory;
import com.xby.rpc.proxy.RpcClientProxy;
import com.xby.rpc.transmission.RpcClient;
import com.xby.rpc.transmission.netty.client.NettyRpcClient;
import com.xby.rpc.transmission.socket.client.SocketRpcClient;

public class ProxyUtils {
    private static final RpcClient rpcClient = SingletonFactory.getInstance(NettyRpcClient.class);
    private static final RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
    public static <T> T getProxy(Class<T> clazz) {
        return rpcClientProxy.getProxy(clazz);
    }
}

