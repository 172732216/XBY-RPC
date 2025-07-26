package com.xby.rpc.loadbalance;

import com.xby.rpc.dto.RpcReq;

import java.util.List;

public interface LoadBalance {
    String select(List<String>list,RpcReq rpcReq);
}
