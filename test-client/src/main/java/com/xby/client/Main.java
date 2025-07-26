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

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args )
    {
        UserService userService = ProxyUtils.getProxy(UserService.class);
        Scanner scanner = new Scanner(System.in);
        ExecutorService executorService= Executors.newFixedThreadPool(20);

        while ( true ){

            int n=scanner.nextInt();
            long id=scanner.nextLong();

            for(int i=0; i<n; i++){
                executorService.execute(()->{
                    try{
                        User user1 = userService.getUser(id);
                        System.out.println(user1);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                });
            }
        }



    }



}
