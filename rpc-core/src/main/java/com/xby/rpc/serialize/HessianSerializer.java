package com.xby.rpc.serialize;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.xby.rpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Author Mr.Pan
 * @Date 2025/3/2
 **/
@Slf4j
public class HessianSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            HessianOutput hessianOutput = new HessianOutput(os);
            hessianOutput.writeObject(obj);

            log.info("========使用Hessian做序列化==========");
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)) {
            HessianInput input = new HessianInput(is);
            Object o = input.readObject();

            log.info("========使用Hessian做反序列化==========");
            return clazz.cast(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
