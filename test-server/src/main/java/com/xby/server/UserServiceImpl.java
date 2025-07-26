package com.xby.server;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.xby.api.User;
import com.xby.api.UserService;
import com.xby.rpc.annotation.Limit;

public class UserServiceImpl implements UserService {
    //@Limit(permitsPerSecond = 5,timeout = 0)
    @Override
    public User getUser(Long id) {
        if(id<0){
            throw new IllegalArgumentException("id小于0");
        }

        return User.builder()
                .id(++id)
                .name("张三")
                .build();
    }
}
