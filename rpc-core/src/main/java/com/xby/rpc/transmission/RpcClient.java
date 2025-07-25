package com.xby.rpc.transmission;

import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.dto.RpcResp;

import java.util.concurrent.Future;

public interface RpcClient {
    Future<RpcResp<?>> sendReq(RpcReq req);
}
