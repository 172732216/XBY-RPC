package com.xby.client;

import com.xby.api.User;
import com.xby.api.UserService;
import com.xby.client.utils.ProxyUtils;
import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.dto.RpcResp;
import com.xby.rpc.proxy.RpcClientProxy;
import com.xby.rpc.transmission.RpcClient;
import com.xby.rpc.transmission.netty.client.NettyRpcClient;
import com.xby.rpc.transmission.socket.client.SocketRpcClient;
import com.xby.rpc.util.ThreadPoolUtils;

import java.util.concurrent.ExecutorService;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args )
    {
//        UserService userService= ProxyUtils.getProxy(UserService.class);
//        User user = userService.getUser(1L);
//        System.out.println(user);
        RpcClient rpcClient=new NettyRpcClient();
        RpcResp<?> rpcResp=rpcClient.sendReq(RpcReq.builder().interfaceName("请求数据").build());


    }



}
