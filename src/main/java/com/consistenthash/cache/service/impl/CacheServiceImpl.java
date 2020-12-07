package com.consistenthash.cache.service.impl;

import com.consistenthash.cache.config.CacheListConfig;
import com.consistenthash.cache.shards.algo.ConsistentHash;
import com.consistenthash.cache.shards.algo.HashFunc;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl {

    private ConsistentHash<StringRedisTemplate> consistentHash;

    public CacheServiceImpl(HashFunc hashFunc, CacheListConfig config) {
        consistentHash = new ConsistentHash<>(hashFunc, config.getList(), 10);
    }
}
