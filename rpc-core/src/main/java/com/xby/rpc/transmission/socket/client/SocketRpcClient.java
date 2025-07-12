package com.xby.rpc.transmission.socket.client;

import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.dto.RpcResp;
import com.xby.rpc.transmission.RpcClient;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
@Slf4j
public class SocketRpcClient implements RpcClient {
    private final String host;
    private final int port;
    public SocketRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    @Override
    public RpcResp<?> sendReq(RpcReq req) {
        try(Socket socket = new Socket(host, port);) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(req);
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Object o=in.readObject();
            return (RpcResp<?>)o;
        } catch (Exception e) {
            log.error("发送rpc请求失败", e);
        }

        return null;
    }
}
