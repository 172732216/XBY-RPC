package com.xby.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public enum VersionType {
    VERSION1((byte)1,"版本1");

    private final byte code;
    private final String desc;
}
