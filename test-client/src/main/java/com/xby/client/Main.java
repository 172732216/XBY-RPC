package com.xby.client;

import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.dto.RpcResp;
import com.xby.rpc.transmission.RpcClient;
import com.xby.rpc.transmission.socket.client.SocketRpcClient;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args )
    {
        RpcClient rpcClient=new SocketRpcClient("127.0.0.1",8888);
        RpcReq req = RpcReq.builder()
                .reqId("1213")
                .interfaceName("com.xby.api.UserService")
                .methodName("getUser")
                .params(new Object[]{1L})
                .paramTypes(new Class[]{Long.class})
                .build();
        RpcResp<?> rpcResp = rpcClient.sendReq(req);
        System.out.println(rpcResp.getData());
    }
//    private static <T> T invoke(Long id) {
//        RpcClient rpcClient=new SocketRpcClient("127.0.0.1",8888);
//        RpcReq req = RpcReq.builder()
//                .reqId("1213")
//                .interfaceName("com.xby.api.UserService")
//                .methodName("getUser")
//                .params(new Object[]{id})
//                .paramTypes(new Class[]{Long.class})
//                .build();
//        RpcResp<?> rpcResp = rpcClient.sendReq(req);
//        User user = (User)rpcResp.getData();
//        System.out.println(user);
//    }
}
