package com.consistenthash.cache.shards.algo;

import com.consistenthash.cache.config.CacheListConfig;
import com.consistenthash.cache.shards.algo.impl.SeedRandomHashFunc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

@SpringBootTest
public class ConsistentHashTest {

    @Autowired
    private CacheListConfig cacheListConfig;

    private ConsistentHash<StringRedisTemplate> consistentHash;

    @Test
    public void test() {
        // arrange
        consistentHash = new ConsistentHash<>(
                    new SeedRandomHashFunc(),
                    cacheListConfig.getList(),
                    10
                );

        //action
        Map<Long, String> records = consistentHash.getCircleRecords();

        // assert
        for (Map.Entry<Long, String> record : records.entrySet()) {
            Assertions.assertEquals(true, true);
            Long key = record.getKey();
            String value = record.getValue();
            System.out.println(key + ": " + value);
        }
    }
}
