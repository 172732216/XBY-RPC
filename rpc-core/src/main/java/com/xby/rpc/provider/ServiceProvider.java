package com.xby.rpc.provider;

public interface ServiceProvider {
    void publishService(Object service);

    Object getService(String rpcServiceName);
}
