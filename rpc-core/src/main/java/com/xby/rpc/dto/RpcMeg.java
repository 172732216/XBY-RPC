package com.xby.rpc.dto;

import com.xby.rpc.enums.CompressType;
import com.xby.rpc.enums.MsgType;
import com.xby.rpc.enums.SerializeType;
import com.xby.rpc.enums.VersionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RpcMeg implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer reqId;
    private VersionType versionType;
    private MsgType msgType;
    private SerializeType serializeType;
    private String msg;
    private CompressType compressType;
    private Object data;
}
