package com.xby.rpc.compress;

public interface Compress {
    byte[] compress(byte[] data);
    byte[] decompress(byte[] data);
}
