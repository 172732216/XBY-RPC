package com.xby.rpc.transmission.socket.server;

import com.xby.rpc.config.RpcServiceConfig;
import com.xby.rpc.constant.RpcConstant;
import com.xby.rpc.factory.SingletonFactory;
import com.xby.rpc.handle.RpcReqHander;
import com.xby.rpc.provider.ServiceProvider;
import com.xby.rpc.provider.impl.ZkServiceProvider;
import com.xby.rpc.transmission.RpcServer;
import com.xby.rpc.util.ShutdownHookUtils;
import com.xby.rpc.util.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

@Slf4j
public class SocketRpcServer implements RpcServer {
    private final int port;
    private final ServiceProvider serviceProvider;
    private final RpcReqHander rpcReqHander;
    private final ExecutorService executor;

    public SocketRpcServer(){
        this(RpcConstant.SERVER_PORT);
    }

    public SocketRpcServer(int port) {
        this(port, SingletonFactory.getInstance(ZkServiceProvider.class));
    }
    public SocketRpcServer(int port, ServiceProvider serviceProvider) {
        this.port = port;
        this.serviceProvider = serviceProvider;
        this.rpcReqHander=new RpcReqHander(serviceProvider);
        this.executor= ThreadPoolUtils.createIoIntensiveThreadPool("socket-rpc-server-");
    }

    @Override
    public void start() {
        ShutdownHookUtils.clearAll();
        try(ServerSocket serverSocket=new ServerSocket(8888)){
            log.info("服务启动，端口: {}", port);
            Socket socket;
            while((socket=serverSocket.accept())!=null){
               executor.submit(new SocketReqHandler(socket,rpcReqHander));
            }
        }catch (Exception e){
            log.error("服务端异常",e);
        }
    }

    @Override
    public void publishService(RpcServiceConfig config) {
        serviceProvider.publishService(config);
    }


}
