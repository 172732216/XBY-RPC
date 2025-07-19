package com.xby.rpc.transmission;

import com.xby.rpc.config.RpcServiceConfig;

public interface RpcServer {
    void start();

    void publishService(RpcServiceConfig config);
}
