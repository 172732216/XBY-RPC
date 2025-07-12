package com.xby.rpc.transmission;

import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.dto.RpcResp;

public interface RpcClient {
    RpcResp<?> sendReq(RpcReq req);
}
