package com.xby.rpc.proxy;

import cn.hutool.core.util.IdUtil;
import com.xby.rpc.config.RpcServiceConfig;
import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.dto.RpcResp;
import com.xby.rpc.enums.RpcRespStatus;
import com.xby.rpc.exception.RpcException;
import com.xby.rpc.transmission.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.concurrent.Future;

public class RpcClientProxy implements InvocationHandler {
    private final RpcClient rpcClient;
    private final RpcServiceConfig config;

    public RpcClientProxy(RpcClient rpcClient) {
        this(rpcClient, new RpcServiceConfig());

    }

    public RpcClientProxy(RpcClient rpcClient,RpcServiceConfig config) {
        this.rpcClient = rpcClient;
        this.config=config;
    }
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcReq rpcReq = RpcReq.builder()
                .reqId(IdUtil.fastSimpleUUID())
                .interfaceName(method.getDeclaringClass().getCanonicalName())
                .methodName(method.getName())
                .params(args)
                .paramTypes(method.getParameterTypes())
                .version(config.getVersion())
                .group(config.getGroup())
                .build();
        Future<RpcResp<?>> future = rpcClient.sendReq(rpcReq);
        RpcResp<?> rpcResp = future.get();
        check(rpcReq,rpcResp);
        return rpcResp.getData();
    }

    private void check(RpcReq req,RpcResp<?> rpcResp) {
        if(Objects.isNull(rpcResp)){
            throw new RpcException("rpcResp为空");
        }
        if(!Objects.equals(req.getReqId(), rpcResp.getReqId())){
            throw new RpcException("请求和响应的id不一致");
        }
        if(RpcRespStatus.isFailed(rpcResp.getCode())){
            throw new RpcException("响应值为失败："+rpcResp.getMsg());
        }
    }
}
