package com.xby.rpc.transmission.socket.server;

import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.dto.RpcResp;
import com.xby.rpc.handle.RpcReqHander;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
@AllArgsConstructor
public class SocketReqHandler implements Runnable {
    private final Socket socket;
    private final RpcReqHander rpcReqHander;
    @SneakyThrows
    @Override
    public void run(){
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        RpcReq rpcReq = (RpcReq) inputStream.readObject();

        Object data=rpcReqHander.invoke(rpcReq);


        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        RpcResp<?> rpcResp = RpcResp.success(rpcReq.getReqId(), data);
        outputStream.writeObject(rpcResp);
        outputStream.flush();
    }
}
