package com.consistenthash.cache.service;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface CacheService {
    /**
     * 增加 shard
     * @param stringRedisTemplate
     */
    void addShard(StringRedisTemplate stringRedisTemplate);

    /**
     * 移除 shard
     * @param stringRedisTemplate
     */
    void removeShard(StringRedisTemplate stringRedisTemplate);

    /**
     * 根據 key 取得特定的 shard
     * @param key
     * @return
     */
    RedisTemplate getShard(String key);

    Map<Long, String> getShards();

    /**
     * ==============================================
     * 以下為針對 redis template 的操作
     * ==============================================
     */

    void set(String keyFormat, Object value, Object... keyValues);

    void setExpire(String keyFormat, Object value, long time, Object... keyValues);

    long getExpire(String keyFormat, Object... keyValues);

    <T> T get(String keyFormat, Object... keyValues);

    Object getObject(String keyFormat, Object... keyValues);

    Long increment(String keyFormat, long number, Object... keyValues);

    Long incrementObject(String keyFormat, long number, Object... keyValues);

    boolean setIfAbsent(String keyFormat, Object value, Object... keyValues);

    String getAndSet(String keyFormat, String value, Object... keyValues);

    boolean expire(String keyFormat, long time, TimeUnit type, Object... keyValues);

    void delete(String keyFormat, Object... keyValues);

    Set getSet(String keyFormat);

    Long add(String keyFormat, String values, Object... keyValues);

    boolean addZSet(String keyFormat, Object value, double score, Object... keyValues);


    Set<Object> rangeSet(String keyFormat, long start, long end, Object... keyValues);

    Object sPop(String keyFormat, Object... keyValues);

    boolean exists(String keyFormat, Object... keyValues);

    boolean hExists(String keyFormat, String field, Object... keyValues);

    void hmSet(String keyFormat, Object hashKey, Object value, Object... keyValues);

    Object hmGet(String keyFormat, Object hashKey, Object... keyValues);

    Object hmDel(String keyFormat, Object hashKey, Object... keyValues);

    Long hIncrBy(String keyFormat, Object field, long increment, Object... keyValues);

    long getAndIncrFromHash(String keyFormat, String field, Object... keyValues);

    String getStringFromHash(String keyFormat, String field, Object... keyValues);
}
