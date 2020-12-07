package com.consistenthash.cache.service.impl;

import com.alibaba.fastjson.JSON;
import com.consistenthash.cache.config.CacheListConfig;
import com.consistenthash.cache.service.CacheService;
import com.consistenthash.cache.shards.algo.ConsistentHash;
import com.consistenthash.cache.shards.algo.HashFunc;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService {

    private static volatile ConsistentHash<StringRedisTemplate> consistentHash;

    public CacheServiceImpl(HashFunc hashFunc, CacheListConfig config) {
        consistentHash = new ConsistentHash<>(hashFunc, config.getList(), 30);
    }

    /**
     * 增加 shard
     * @param stringRedisTemplate
     */
    @Override
    public void addShard(StringRedisTemplate stringRedisTemplate) {
        consistentHash.add(stringRedisTemplate);
        // TODO then, data migration?
    }

    /**
     * 移除 shard
     * @param stringRedisTemplate
     */
    @Override
    public void removeShard(StringRedisTemplate stringRedisTemplate) {
        // TODO do data migration first?
        consistentHash.remove(stringRedisTemplate);
    }

    /**
     * 根據 key 取得特定的 shard
     * @param key
     * @return
     */
    @Override
    public RedisTemplate getShard(String key) {
        return consistentHash.get(key);
    }

    @Override
    public String getShardId(String key) {
        return consistentHash.getCircleRecord(key);
    }

    @Override
    public Map<Long, String> getCircleShardIds() {
        return consistentHash.getCircleRecords();
    }

    /**
     * ==============================================
     * 以下為針對 redis template 的操作
     * ==============================================
     */

    @Override
    public void set(String keyFormat, Object value, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        getShard(key).opsForValue().set(key, value);
    }

    @Override
    public void setExpire(String keyFormat, Object value, long time, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        getShard(key).opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    @Override
    public long getExpire(String keyFormat, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public <T> T get(String keyFormat, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return (T) getShard(key).opsForValue().get(key);
    }

    @Override
    public Object getObject(String keyFormat, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).opsForValue().get(key);
    }

    @Override
    public Long increment(String keyFormat, long number, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).opsForValue().increment(key, number);
    }

    @Override
    public Long incrementObject(String keyFormat, long number, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).opsForValue().increment(key, number);
    }

    @Override
    public boolean setIfAbsent(String keyFormat, Object value, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).opsForValue().setIfAbsent(key, value);
    }

    @Override
    public String getAndSet(String keyFormat, String value, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return (String) getShard(key).opsForValue().getAndSet(key, value);
    }

    @Override
    public boolean expire(String keyFormat, long time, TimeUnit type, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).boundValueOps(key).expire(time, type);
    }

    @Override
    public void delete(String keyFormat, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        getShard(key).delete(key);
    }

    @Override
    public Set getSet(String keyFormat) {
        String key = String.format(keyFormat);
        return getShard(key).opsForSet().members(key);
    }

    @Override
    public Long add(String keyFormat, String values, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).opsForSet().add(key, values);
    }

    @Override
    public boolean addZSet(String keyFormat, Object value, double score, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).opsForZSet().add(key, value, score);
    }

    @Override
    public Set<Object> rangeSet(String keyFormat, long start, long end, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).opsForZSet().range(key, start, end);
    }

    @Override
    public Object sPop(String keyFormat, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).opsForSet().pop(key);
    }

    @Override
    public boolean exists(String keyFormat, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).hasKey(key);
    }

    @Override
    public boolean hExists(String keyFormat, String field, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).opsForHash().hasKey(key, field);
    }

    @Override
    public void hmSet(String keyFormat, Object hashKey, Object value, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        getShard(key).opsForHash().put(key, hashKey, value);
    }

    @Override
    public Object hmGet(String keyFormat, Object hashKey, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).opsForHash().get(key, hashKey);
    }

    @Override
    public Object hmDel(String keyFormat, Object hashKey, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).opsForHash().delete(key, hashKey);
    }

    @Override
    public Long hIncrBy(String keyFormat, Object field, long increment, Object... keyValues) {
        String key = String.format(keyFormat, keyValues);
        return getShard(key).opsForHash().increment(key, field, increment);
    }

    @Override
    public long getAndIncrFromHash(String keyFormat, String field, Object... keyValues) {
        long result = 0;
        if (this.hExists(keyFormat, field, keyValues)) {
            return this.hIncrBy(keyFormat, field, 1, keyValues);
        }
        this.hmSet(keyFormat, field, "1", keyValues);
        return result;
    }

    @Override
    public String getStringFromHash(String keyFormat, String field, Object... keyValues) {
        if (!this.hExists(keyFormat, field, keyValues)) {
            return null;
        }
        String json = (String) this.hmGet(keyFormat, field, keyValues);
        return json == null ? null : JSON.parseObject(json, String.class);
    }

    public static void main(String[] args) {
        String key = String.format("val1", "val2");
        System.out.println(key);
    }
}
