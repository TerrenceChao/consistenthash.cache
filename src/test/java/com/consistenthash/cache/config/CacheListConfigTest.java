package com.consistenthash.cache.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@SpringBootTest
public class CacheListConfigTest {

    // first redis's connection
    @Value("${consistenthash.caches.redis1.host}")
    private String host1;

    @Value("${consistenthash.caches.redis1.port}")
    private Integer port1;


    // second redis's connection
    @Value("${consistenthash.caches.redis2.host}")
    private String host2;

    @Value("${consistenthash.caches.redis2.port}")
    private Integer port2;


    // third redis's connection
    @Value("${consistenthash.caches.redis3.host}")
    private String host3;

    @Value("${consistenthash.caches.redis3.port}")
    private Integer port3;

    @Autowired
    private CacheListConfig cacheListConfig;

    @Test
    public void test() {
        // arrange
        List<StringRedisTemplate> cacheList = cacheListConfig.getList();
        LettuceConnectionFactory factory;

        // first redis
        // action
        StringRedisTemplate redis1 = cacheList.get(0);
        factory = (LettuceConnectionFactory) redis1.getConnectionFactory();

        // assert
        Assertions.assertNotNull(factory);
        Assertions.assertEquals(port1, factory.getPort());
        Assertions.assertEquals(host1, factory.getHostName());


        // second redis
        // action
        StringRedisTemplate redis2 = cacheList.get(1);
        factory = (LettuceConnectionFactory) redis2.getConnectionFactory();

        // assert
        Assertions.assertNotNull(factory);
        Assertions.assertEquals(port2, factory.getPort());
        Assertions.assertEquals(host2, factory.getHostName());


        // third redis
        // action
        StringRedisTemplate redis3 = cacheList.get(2);
        factory = (LettuceConnectionFactory) redis3.getConnectionFactory();

        // assert
        Assertions.assertNotNull(factory);
        Assertions.assertEquals(port3, factory.getPort());
        Assertions.assertEquals(host3, factory.getHostName());
    }
}
