package com.xby.rpc.handle;

import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.provider.ServiceProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class RpcReqHander {
    private final ServiceProvider serviceProvider;

    public RpcReqHander(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @SneakyThrows
    public Object invoke(RpcReq rpcReq)  {
        String rpcServiceName=rpcReq.rpcServiceName();
        Object service=serviceProvider.getService(rpcServiceName);

        log.debug("获取到对应服务:{}",service.getClass().getCanonicalName());
        Method method = service.getClass().getMethod(rpcReq.getMethodName(), rpcReq.getParamTypes());
        return method.invoke(service, rpcReq.getParams());
    }
}
