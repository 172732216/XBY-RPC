package com.xby.rpc.registry.impl;

import cn.hutool.core.util.StrUtil;
import com.xby.rpc.constant.RpcConstant;
import com.xby.rpc.factory.SingletonFactory;
import com.xby.rpc.registry.ServiceRegistry;
import com.xby.rpc.registry.zk.ZkClient;
import com.xby.rpc.util.IPUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class ZkServiceRegistry implements ServiceRegistry {
    private final ZkClient zkClient;

    public ZkServiceRegistry() {
        this(SingletonFactory.getInstance(ZkClient.class));
    }
    public ZkServiceRegistry(ZkClient zkClient) {
        this.zkClient = zkClient;
    }
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress address) {
        log.info("服务注册,rpcServiceName:{},address:{}", rpcServiceName, address);

        String path= RpcConstant.ZK_RPC_ROOT_PATH+ StrUtil.SLASH +rpcServiceName+StrUtil.SLASH+IPUtils.toIpPort(address);
        zkClient.createPersistentNode(path);
    }


}
