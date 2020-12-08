package com.consistenthash.cache.shards.algo;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConsistentHash<N> {

    private static HashFunc hashFunc;
    private final int numberOfReplicas;
    private final ConcurrentSkipListMap<Long, N> circle = new ConcurrentSkipListMap<>();

    public ConsistentHash(HashFunc<N> hashFunc,
                          Collection<N> nodes,
                          int numberOfReplicas) {
        this.hashFunc = hashFunc;
        this.numberOfReplicas = numberOfReplicas;

        for (N node : nodes) {
            add(node);
        }
    }

    /**
     * @param node
     */
    public void add(N node) {
        String record = hashFunc.toRecord(node);
        hashFunc.seed(record);

        for (int i = 0; i < numberOfReplicas; i++) {
//            long hash = hashFunc.hash(pattern + i); // 是否 "+ i", 端視妳的 HashFunc 是否可設定 seed
            long hash = hashFunc.hash();
            if (! circle.containsKey(hash)) {
                circle.put(hash, node);
            }
        }
    }

    public void remove(N node) {
        String record = hashFunc.toRecord(node);
        hashFunc.seed(record);

        for (int i = 0; i < numberOfReplicas; i++) {
//            long hash = hashFunc.hash(pattern + i); // 是否 "+ i", 端視妳的 HashFunc 是否可設定 seed
            long hash = hashFunc.hash();
            N circleNode = circle.get(hash);
            if (Objects.isNull(circleNode)) {
                continue;
            }

            String recordB = hashFunc.toRecord(circleNode);
            if (recordB.equals(record)) {
                circle.remove(hash);
            }
        }
    }

    /**
     * TODO consider concurrent issue
     * @param key
     * @return
     */
    public N get(String key) {
        if (circle.isEmpty()) {
            return null;
        }

        long code = hashFunc.hash(key);
        if (! circle.containsKey(code)) {
            // TODO what's the performance of tailMap?
            ConcurrentNavigableMap<Long, N> tailMap = circle.tailMap(code);
            code = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }

        return circle.get(code);
    }

    public String getCircleRecord(String key) {
        return hashFunc.toRecord(get(key));
    }

    public Map<Long, String> getCircleRecords() {
        Map<Long, String> records = new HashMap<>();
        for (Map.Entry<Long, N> shard : circle.entrySet()) {
            Long hash = shard.getKey();
            N node = shard.getValue();
            records.put(hash, hashFunc.toRecord(node));
        }

        return records;
    }

    public Integer getNumberOfReplicas() {
        return numberOfReplicas;
    }
}
