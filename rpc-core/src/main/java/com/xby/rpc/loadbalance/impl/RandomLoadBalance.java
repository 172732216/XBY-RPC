package com.xby.rpc.loadbalance.impl;

import cn.hutool.core.util.RandomUtil;
import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.loadbalance.LoadBalance;

import java.util.List;

public class RandomLoadBalance implements LoadBalance {
    @Override
    public String select(List<String>list, RpcReq rpcReq) {

        return RandomUtil.randomEle(list);
    }
}
