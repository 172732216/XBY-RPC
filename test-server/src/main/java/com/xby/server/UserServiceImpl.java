package com.xby.server;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.xby.api.User;
import com.xby.api.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getUser(Long id) {
        return User.builder()
                .id(++id)
                .name("张三")
                .build();
    }
}
