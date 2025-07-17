package com.xby.rpc.registry;

import java.net.InetSocketAddress;

public interface ServiceRegistry {
    void registerService(String rpcServiceName, InetSocketAddress address);

    void clearAll();
}
