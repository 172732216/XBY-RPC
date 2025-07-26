package com.xby.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {
    /**
     *
     * 每秒支持多少访问请求
     */
    double permitsPerSecond();

    long timeout();
}
