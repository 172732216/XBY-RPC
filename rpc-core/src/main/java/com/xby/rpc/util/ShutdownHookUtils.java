package com.xby.rpc.util;

import com.xby.rpc.factory.SingletonFactory;
import com.xby.rpc.registry.impl.ZkServiceRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShutdownHookUtils {
    public static void clearAll() {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            log.info("系统结束运行，清理资源");
            ZkServiceRegistry serviceRegistry = SingletonFactory.getInstance(ZkServiceRegistry.class);
            serviceRegistry.clearAll();
            ThreadPoolUtils.shutdownAll();
        })) ;
    }
}
