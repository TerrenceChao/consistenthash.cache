package com.consistenthash.cache.shards.algo;

import org.springframework.data.redis.core.StringRedisTemplate;

public interface ShardNode {
    String toPattern();
    StringRedisTemplate getRedis();
}
