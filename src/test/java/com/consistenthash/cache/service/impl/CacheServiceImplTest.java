package com.consistenthash.cache.service.impl;

import com.consistenthash.cache.service.CacheService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;


@SpringBootTest
public class CacheServiceImplTest {

    @Autowired
    private CacheService cacheService;

    @Test
    public void test() {
        // arrange
        String key = "Greeting ";
        String value = "Hello World! ";
        int times = 1000;

        // action
        for (int i = 0; i < times; i++) {
            String k = key + i;
            String val = value + i;
            cacheService.set(k, val);
        }

        // assert
        for (int i = 0; i < times; i++) {
            String k = key + i;
            String expectedVal = value + i;
            String actualVal = cacheService.get(k);
            Assertions.assertEquals(expectedVal, actualVal);

            System.out.println(k + " on " + cacheService.getShardId(k));
        }

        for (Map.Entry<Long, String> shard : cacheService.getCircleShardIds().entrySet()) {
            Long skey = shard.getKey();
            String svalue = shard.getValue();
            System.out.println("key: " + skey + "\tvalue: " + svalue);
        }
    }
}
