package com.consistenthash.cache.shards.algo.impl;

import com.consistenthash.cache.shards.algo.HashFunc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * google keyword: "java seed random"
 * ref: http://tw.gitbook.net/java/util/timezone_setseed.html
 */
@SpringBootTest
public class SeedRandomHashFuncTest {

    HashFunc hashFunc;

    /**
     * 測試 hash 的總數 (無意義的測試!?)
     */
    @Test
    public void testTotalHashValues() {
        // arrange
        String record = "localhost:6379";
        hashFunc = new SeedRandomHashFunc();
        Set<Long> records = new HashSet<>();
        int totalHashValues = 100;

        // action
        hashFunc.seed(record);
        for (int i = 1; i <= totalHashValues; i++) {
            long hash = hashFunc.hash();
            records.add(hash);
        }

        // assert
        Assertions.assertEquals(totalHashValues, records.size());
    }

    /**
     * 測試在同樣的 seed 下，所產生的 hash 將一致
     * google keyword: "java seed random"
     * ref: http://tw.gitbook.net/java/util/timezone_setseed.html
     */
    @Test
    public void testWithSameRecord() {
        // arrange
        String record = "127.0.0.1:6380";
        hashFunc = new SeedRandomHashFunc();
        Map<Integer, Long> recordMap = new HashMap<>();
        int totalHashValues = 50;

        // action
        hashFunc.seed(record);
        for (int idx = 1; idx <= totalHashValues; idx++) {
            long hash = hashFunc.hash();
            recordMap.put(idx, hash);
        }

        // assert (seed again with same record)
        hashFunc.seed(record);
        for (int idx = 1; idx <= totalHashValues; idx++) {
            long actualHash = hashFunc.hash();
            long expectedHash = recordMap.get(idx);
            Assertions.assertEquals(expectedHash, actualHash);
        }
    }

    /**
     * 測試在不同的 seed 下，hash 絕不會碰撞!? (無意義的測試!?)
     */
    @Test
    public void testWithDiffRecord() {
        // arrange
        String recordA = "localhost:6379";
        hashFunc = new SeedRandomHashFunc();
        Set<Long> records = new HashSet<>();
        int totalHashValues = 1000;

        // action
        hashFunc.seed(recordA);
        for (int i = 1; i <= totalHashValues; i++) {
            long hash = hashFunc.hash();
            records.add(hash);
        }

        // assert
        String recordB = "127.0.0.1:6380";
        hashFunc.seed(recordB);
        for (int i = 1; i <= totalHashValues; i++) {
            long hash = hashFunc.hash();
            Assertions.assertFalse(records.contains(hash));
        }
    }
}
