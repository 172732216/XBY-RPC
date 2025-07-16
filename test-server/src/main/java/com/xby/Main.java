package com.xby;

import com.xby.api.User;
import com.xby.api.UserService;
import com.xby.rpc.config.RpcServiceConfig;
import com.xby.rpc.proxy.RpcClientProxy;
import com.xby.rpc.transmission.RpcService;
import com.xby.rpc.transmission.socket.server.SocketRpcServer;
import com.xby.server.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        RpcServiceConfig config = new RpcServiceConfig(new UserServiceImpl());

        RpcService rpcService=new SocketRpcServer();
        rpcService.publishService(config);

        rpcService.start();




    }
}
