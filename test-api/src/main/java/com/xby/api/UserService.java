package com.xby.api;

import com.xby.rpc.annotation.Breaker;
import com.xby.rpc.annotation.Retry;

public interface UserService {
    //@Retry
    @Breaker(windowTime = 30000)
    User getUser(Long id);
}
