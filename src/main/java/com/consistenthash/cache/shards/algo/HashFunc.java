package com.consistenthash.cache.shards.algo;

public interface HashFunc<N> {
    String toRecord(N node);
    void seed(String pattern);
    long hash();
    long hash(String pattern);
}
