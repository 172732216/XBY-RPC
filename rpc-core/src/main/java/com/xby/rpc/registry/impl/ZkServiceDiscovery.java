package com.xby.rpc.registry.impl;

import cn.hutool.core.util.StrUtil;
import com.xby.rpc.constant.RpcConstant;
import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.factory.SingletonFactory;
import com.xby.rpc.loadbalance.LoadBalance;
import com.xby.rpc.loadbalance.impl.RandomLoadBalance;
import com.xby.rpc.registry.ServiceDiscovery;
import com.xby.rpc.registry.zk.ZkClient;
import com.xby.rpc.util.IPUtils;

import java.net.InetSocketAddress;
import java.util.List;

public class ZkServiceDiscovery implements ServiceDiscovery {
    private final ZkClient zkClient;
    private final LoadBalance loadBalance;

    public ZkServiceDiscovery() {
        this(SingletonFactory.getInstance(ZkClient.class),
             SingletonFactory.getInstance(RandomLoadBalance.class));
    }

    public ZkServiceDiscovery(ZkClient zkClient, LoadBalance loadBalance) {
        this.zkClient = zkClient;
        this.loadBalance = loadBalance;
    }

    @Override
    public InetSocketAddress lookupService(RpcReq rpcReq) {
        String path= RpcConstant.ZK_RPC_ROOT_PATH+ StrUtil.SLASH+rpcReq.rpcServiceName();

        List<String> children=zkClient.getChildrenNode(path);
        String address = loadBalance.select(children,rpcReq);

        return IPUtils.toInetSocketAddress(address);
    }
}
