package com.consistenthash.cache.shards.algo.impl;

import com.consistenthash.cache.shards.algo.HashFunc;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Component
public class SeedRandomHashFunc implements HashFunc<StringRedisTemplate> {

    private Random random;
    private final String CHAR66 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ:;,.";
    private final Map<Character, Integer> dict = new HashMap<>();

    public SeedRandomHashFunc() {
        for (int i = 0; i < CHAR66.length(); i++) {
            dict.put(CHAR66.charAt(i), i);
        }
    }

    @Override
    public String toRecord(StringRedisTemplate node) {
        LettuceConnectionFactory factory = (LettuceConnectionFactory) node.getConnectionFactory();
        String host = factory.getHostName();
        String port = String.valueOf(factory.getPort());

        return host.concat(":").concat(port);
    }

    @Override
    public void seed(String pattern) {
        long seed = encode(pattern);
        random = new Random();
        random.setSeed(seed);
    }

    @Override
    public long hash() {
        return Objects.nonNull(random) ? random.nextLong() : -1;
    }

    @Override
    public long hash(String pattern) {
        seed(pattern);
        return random.nextLong();
    }

    private long encode(String pattern) {
        long code = 0;

        int len = pattern.length();
        for (int i = 0; i < len; i++) {
            Character c = pattern.charAt(i);

            if (dict.containsKey(c)) {
                code += dict.get(c) * (i + 1);
            } else {
                code += i * (i + 1);
            }
        }

        return code;
    }

}
