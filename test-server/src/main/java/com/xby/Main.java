package com.xby;

import com.xby.rpc.transmission.RpcService;
import com.xby.rpc.transmission.socket.server.SocketRpcServer;

public class Main {
    public static void main(String[] args) {
        RpcService rpcService=new SocketRpcServer(8888);
        rpcService.start();
    }
}
