package com.xby.rpc.registry;

import com.xby.rpc.dto.RpcReq;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {
    InetSocketAddress lookupService(RpcReq rpcReq);
}
