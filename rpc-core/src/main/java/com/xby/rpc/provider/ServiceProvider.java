package com.xby.rpc.provider;

import com.xby.rpc.config.RpcServiceConfig;

public interface ServiceProvider {
    void publishService(RpcServiceConfig config);

    Object getService(String rpcServiceName);
}
