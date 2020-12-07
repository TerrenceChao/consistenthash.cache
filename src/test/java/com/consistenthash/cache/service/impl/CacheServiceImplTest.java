package com.consistenthash.cache.service.impl;

import com.consistenthash.cache.service.CacheService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class CacheServiceImplTest {

    @Autowired
    private CacheService cacheService;

//    @Test
//    public void test() {
//        // arrange
//        String value = "hello world";
//
//        // action
//        cacheService.set("test", value);
//
//        // assert
//        String expected = "hello world";
//        Assertions.assertEquals(expected, cacheService.get("test"));
//    }
}
