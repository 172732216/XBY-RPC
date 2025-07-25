package com.xby;

import com.xby.rpc.config.RpcServiceConfig;
import com.xby.rpc.transmission.RpcServer;
import com.xby.rpc.transmission.netty.server.NettyRpcServer;
import com.xby.rpc.transmission.socket.server.SocketRpcServer;
import com.xby.server.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        RpcServiceConfig config = new RpcServiceConfig(new UserServiceImpl());
//
//        RpcServer rpcService=new SocketRpcServer();
//        rpcService.publishService(config);
//
//        rpcService.start();
        RpcServer rpcServer = new NettyRpcServer();
        rpcServer.publishService(config);

        rpcServer.start();



    }
}
