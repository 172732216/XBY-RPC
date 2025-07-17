package com.xby.rpc.registry.zk;

import cn.hutool.core.util.StrUtil;
import com.xby.rpc.constant.RpcConstant;
import com.xby.rpc.util.IPUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ZkClient {
    private static final int BASE_SLEEP_TIME = 1000;

    private static final int MAX_RETRIES=3;

    private final CuratorFramework client;

    private static final Map<String,List<String>> SERVICE_ADDRESS_CACHE = new ConcurrentHashMap<>();

    private static final Set<String> SERVICE_ADDRESS_SET=ConcurrentHashMap.newKeySet();
    public ZkClient(){
        this(RpcConstant.ZK_IP, RpcConstant.ZK_PORT);
    }


    public ZkClient(String hostname,int port){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);

        client= CuratorFrameworkFactory.builder()
                .connectString(hostname+ StrUtil.COLON+port)
                .retryPolicy(retryPolicy)
                .build();

        log.info("开始连接zk...");

        this.client.start();
        log.info("zk连接成功");
    }

    @SneakyThrows
    public void createPersistentNode(String path){

        if(StrUtil.isBlank(path)){
            throw new IllegalArgumentException("path为空");
        }

        if(SERVICE_ADDRESS_SET.contains(path)){
            log.info("该节点已存在");
            return;
        }

        if(client.checkExists().forPath(path)!=null){
            SERVICE_ADDRESS_SET.add(path);
            log.info("该节点已存在");
            return;
        }

        log.info("创建节点:{}",path);
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path);

        SERVICE_ADDRESS_SET.add(path);
    }

    public void clearAll(InetSocketAddress address){
        if(Objects.isNull(address)){
            throw new IllegalArgumentException("address为空");
        }
        SERVICE_ADDRESS_SET.forEach(path->{
            if(path.endsWith(IPUtils.toIpPort(address))){
                log.debug("zk删除节点:{}",path);
                try {
                    client.delete().deletingChildrenIfNeeded().forPath(path);
                } catch (Exception e) {
                    log.debug("zk删除失败:{}",path);
                }

            }
        });
    }

    @SneakyThrows
    public List<String> getChildrenNode(String path){
        if(StrUtil.isBlank(path)){
            throw new IllegalArgumentException("path为空");
        }
        if(SERVICE_ADDRESS_CACHE.containsKey(path)){
            return SERVICE_ADDRESS_CACHE.get(path);
        }
        List<String> children = client.getChildren().forPath(path);
        SERVICE_ADDRESS_CACHE.put(path, children);

        watchNode(path);

        return children;
    }
    @SneakyThrows
    private void watchNode(String path){
        PathChildrenCache pathChildrenCache=new PathChildrenCache(client,path,true);
        PathChildrenCacheListener pathChildrenCacheListener=(curClient, event) ->{
            List<String> children = curClient.getChildren().forPath(path);
            SERVICE_ADDRESS_CACHE.put(path, children);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }

}
