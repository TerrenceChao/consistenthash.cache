package com.consistenthash.cache.shards.algo.impl;

import com.consistenthash.cache.shards.algo.ShardNode;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisShardNode implements ShardNode {

    private StringRedisTemplate stringRedisTemplate;

    public RedisShardNode(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String toPattern() {
        LettuceConnectionFactory factory = (LettuceConnectionFactory) stringRedisTemplate.getConnectionFactory();
        String host = factory.getHostName();
        String port = String.valueOf(factory.getPort());

        return host.concat(":").concat(port);
    }

    @Override
    public StringRedisTemplate getRedis() {
        return stringRedisTemplate;
    }
}
