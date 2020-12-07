package com.consistenthash.cache.shards.algo.impl;

import com.consistenthash.cache.shards.algo.HashFunc;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class SeedRandomHashFunc implements HashFunc {

    private Random random;
    private final String CHAR66 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ:;,.";
    private final Map<Character, Integer> dict = new HashMap<>();

    public SeedRandomHashFunc() {
        for (int i = 0; i < CHAR66.length(); i++) {
            dict.put(CHAR66.charAt(i), i);
        }
    }

    @Override
    public void seed(String pattern) {
        random = new Random();
        long seed = toSeed(pattern);
        random.setSeed(seed);
    }

    @Override
    public Long hash(String pattern) {
        return random.nextLong();
    }

    private long toSeed(String pattern) {
        long seed = 0;

        int len = pattern.length();
        for (int i = 0; i < len; i++) {
            Character c = pattern.charAt(i);
            if (dict.containsKey(c)) {
                seed += dict.get(c) * (i + 1);
            } else {
                seed += i * (i + 1);
            }
        }

        return seed;
    }
}
