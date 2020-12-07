package com.consistenthash.cache.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * ref: https://developpaper.com/spring-boot-multi-data-source-redis-configuration/
 */
@SpringBootTest
public class RedisConfigTest {

    // first redis
    @Value("${consistenthash.caches.redis1.host}")
    private String host1;

    @Value("${consistenthash.caches.redis1.port}")
    private Integer port1;

    @Autowired
    @Qualifier("redis1")
    private StringRedisTemplate redis1;


    // second redis
    @Value("${consistenthash.caches.redis2.host}")
    private String host2;

    @Value("${consistenthash.caches.redis2.port}")
    private Integer port2;

    @Autowired
    @Qualifier("redis2")
    private StringRedisTemplate redis2;


    // third redis
    @Value("${consistenthash.caches.redis3.host}")
    private String host3;

    @Value("${consistenthash.caches.redis3.port}")
    private Integer port3;

    @Autowired
    @Qualifier("redis3")
    private StringRedisTemplate redis3;


    @Test
    public void testRedisConnection() {
        LettuceConnectionFactory factory;

        // first redis
        // arrange
        factory = (LettuceConnectionFactory) redis1.getConnectionFactory();

        // action
//        redis1.opsForValue().set(RedisType.PERSON_INFO.getRealKey("test"), "6379");
//        Assertions.assertEquals("6379", redis1.opsForValue().get(RedisType.PERSON_INFO.getRealKey("test")));
        redis1.opsForValue().set("test", "come from first cache");

        // assert
        Assertions.assertNotNull(factory);
        Assertions.assertEquals(port1, factory.getPort());
        Assertions.assertEquals(host1, factory.getHostName());
        Assertions.assertEquals("come from first cache", redis1.opsForValue().get("test"));


        // second redis
        // arrange
        factory = (LettuceConnectionFactory) redis2.getConnectionFactory();

        // action
        redis2.opsForValue().set("test2", "come from second cache (No. 2)");

        // assert
        Assertions.assertNotNull(factory);
        Assertions.assertEquals(port2, factory.getPort());
        Assertions.assertEquals(host2, factory.getHostName());
        Assertions.assertEquals("come from second cache (No. 2)", redis2.opsForValue().get("test2"));


        // third redis
        // arrange
        factory = (LettuceConnectionFactory) redis3.getConnectionFactory();

        // action
        redis3.opsForValue().set("test3", "My name is 3rd cache");

        // assert
        Assertions.assertNotNull(factory);
        Assertions.assertEquals(port3, factory.getPort());
        Assertions.assertEquals(host3, factory.getHostName());
        Assertions.assertEquals( "My name is 3rd cache", redis3.opsForValue().get("test3"));
    }
}
