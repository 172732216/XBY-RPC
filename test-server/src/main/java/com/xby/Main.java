package com.xby;

import cn.hutool.core.collection.ListUtil;
import com.xby.rpc.config.RpcServiceConfig;
import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.factory.SingletonFactory;
import com.xby.rpc.loadbalance.LoadBalance;
import com.xby.rpc.loadbalance.impl.ConsistentHashLoadBalance;
import com.xby.rpc.transmission.RpcServer;
import com.xby.rpc.transmission.netty.server.NettyRpcServer;
import com.xby.rpc.transmission.socket.server.SocketRpcServer;
import com.xby.server.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
//        RpcServiceConfig config = new RpcServiceConfig(new UserServiceImpl());
////
////        RpcServer rpcService=new SocketRpcServer();
////        rpcService.publishService(config);
////
////        rpcService.start();
//        RpcServer rpcServer = new NettyRpcServer();
//        rpcServer.publishService(config);
//
//        rpcServer.start();

        LoadBalance loadBalance= SingletonFactory.getInstance(ConsistentHashLoadBalance.class);

        List<String> list= ListUtil.of("ip1:port1","ip2:port2","ip3:port3");

        RpcReq rpcReq =RpcReq.builder()
                .interfaceName("test")
                .group("1")
                .version("2")
                .build();
        for(int i=0;i<10;i++){
            String select=loadBalance.select(list,rpcReq);
            System.out.println(select);
        }


    }
}
