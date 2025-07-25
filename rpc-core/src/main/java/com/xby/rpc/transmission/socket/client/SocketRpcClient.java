package com.xby.rpc.transmission.socket.client;

import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.dto.RpcResp;
import com.xby.rpc.factory.SingletonFactory;
import com.xby.rpc.registry.ServiceDiscovery;
import com.xby.rpc.registry.impl.ZkServiceDiscovery;
import com.xby.rpc.transmission.RpcClient;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
public class SocketRpcClient implements RpcClient {
    private final ServiceDiscovery serviceDiscovery;

    public SocketRpcClient(){
        this(SingletonFactory.getInstance(ZkServiceDiscovery.class));
    }
    public SocketRpcClient(ServiceDiscovery serviceDiscovery){
        this.serviceDiscovery = serviceDiscovery;
    }
    @Override
    public Future<RpcResp<?>> sendReq(RpcReq rpcReq) {
        InetSocketAddress address = serviceDiscovery.lookupService(rpcReq);

        try(Socket socket = new Socket(address.getAddress(), address.getPort());) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(rpcReq);
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Object o=in.readObject();
            return CompletableFuture.completedFuture((RpcResp<?>)o);
        } catch (Exception e) {
            log.error("发送rpc请求失败", e);
        }

        return null;
    }
}
