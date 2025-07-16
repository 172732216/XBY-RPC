package com.xby.rpc.transmission;

import com.xby.rpc.config.RpcServiceConfig;

public interface RpcService {
    void start();

    void publishService(RpcServiceConfig config);
}
