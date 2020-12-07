package com.consistenthash.cache.shards.algo;

public interface HashFunc {
    void seed(String pattern);
    Long hash(String pattern);
}
