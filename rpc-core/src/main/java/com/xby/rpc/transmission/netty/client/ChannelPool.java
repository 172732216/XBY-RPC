package com.xby.rpc.transmission.netty.client;
import io.netty.channel.Channel;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ChannelPool {
    private final Map<String, Channel>pool=new ConcurrentHashMap<>();

    public Channel get(InetSocketAddress addr, Supplier<Channel> supplier) {
        String addString=addr.toString();

        Channel channel=pool.get(addString);
        if(channel!=null&&channel.isActive()) {
            return channel;
        }
        Channel newChannel=supplier.get();
        pool.put(addString, newChannel);
        return newChannel;
    }
}
