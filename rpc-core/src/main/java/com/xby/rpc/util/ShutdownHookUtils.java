package com.xby.rpc.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShutdownHookUtils {
    public static void clearAll() {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            log.info("系统结束运行，清理资源");
            ThreadPoolUtils.shutdownAll();
        }) {

        });
    }
}
