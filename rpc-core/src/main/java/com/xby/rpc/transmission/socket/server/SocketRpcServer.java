package com.xby.rpc.transmission.socket.server;

import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.dto.RpcResp;
import com.xby.rpc.transmission.RpcService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class SocketRpcServer implements RpcService {
    private final int port;

    public SocketRpcServer(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        try(ServerSocket serverSocket=new ServerSocket(8888)){
            log.info("Server started on port: {}", port);
            Socket socket;
            while((socket=serverSocket.accept())!=null){
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                RpcReq rpcReq = (RpcReq) inputStream.readObject();

                System.out.println(rpcReq);

                String data="sfsdf12312";
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                RpcResp<String> rpcResp = RpcResp.success(rpcReq.getReqId(), data);
                outputStream.writeObject(rpcResp);
                outputStream.flush();
            }
        }catch (Exception e){
            log.error("服务端异常",e);
        }
    }
}
