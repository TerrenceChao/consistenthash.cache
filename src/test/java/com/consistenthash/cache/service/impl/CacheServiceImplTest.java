package com.consistenthash.cache.service.impl;

import com.consistenthash.cache.service.CacheService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;


@SpringBootTest
public class CacheServiceImplTest {

    @Autowired
    private CacheService cacheService;

    @BeforeEach
    public void before() {
        for (Map.Entry<Long, String> shard : cacheService.getCircleShardIds().entrySet()) {
            Long key = shard.getKey();
            String value = shard.getValue();
            System.out.println("key: " + key + "\tvalue: " + value);
        }
        System.out.println("\n\n");
    }

    @Test
    public void test() {
        // arrange
        String key = "Greeting-";
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

//            System.out.println(cacheService.getShardId(k) + " (k, v): (" + k + ", " + actualVal + ")");
            cacheService.delete(k);
        }
    }
}
