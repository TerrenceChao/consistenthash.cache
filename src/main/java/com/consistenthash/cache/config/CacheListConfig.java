package com.consistenthash.cache.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CacheListConfig {

    @Autowired
    @Qualifier("redis1")
    private StringRedisTemplate redis1;

    @Autowired
    @Qualifier("redis2")
    private StringRedisTemplate redis2;

    @Autowired
    @Qualifier("redis3")
    private StringRedisTemplate redis3;

    private List<StringRedisTemplate> cacheList = new ArrayList<>();

    public List<StringRedisTemplate> getList() {
        cacheList.clear();
        cacheList.add(redis1);
        cacheList.add(redis2);
        cacheList.add(redis3);

        return cacheList;
    }
}
