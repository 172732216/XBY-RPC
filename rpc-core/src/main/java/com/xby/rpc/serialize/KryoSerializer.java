package com.xby.rpc.serialize;

import com.xby.rpc.dto.RpcReq;
import com.xby.rpc.dto.RpcResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.helpers.ThreadLocalMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
@Slf4j
public class KryoSerializer implements Serializer{
    private static final ThreadLocal<Kryo>KRYO_THREAD_LOCAL= ThreadLocal.withInitial(()->{
        Kryo kryo = new Kryo();
        kryo.register(RpcReq.class);
        kryo.register(RpcResp.class);
        return kryo;
    })
    @Override
    public byte[] serialize(Object obj) {
       try(ByteArrayOutputStream oos=new ByteArrayOutputStream();
           Output output=new Output(oos);){
           Kryo kryo=KRYO_THREAD_LOCAL.get();
           kryo.writeObject(output,obj);
           output.flush();
           return oos.toByteArray();
       }catch (Exception e){
            log.error("kryo序列化失败",e);
            throw new RuntimeException(e);
       }finally {
           KRYO_THREAD_LOCAL.remove();
       }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try(ByteArrayInputStream oos=new ByteArrayInputStream();
            Input input=new Input(oos);){
            Kryo kryo=KRYO_THREAD_LOCAL.get();
            return kryo.readObject(input,clazz);
        }catch (Exception e){
            log.error("kryo反序列化失败",e);
            throw new RuntimeException(e);
        }finally {
            KRYO_THREAD_LOCAL.remove();
        }
    }
}
