package com.consistenthash.cache.shards.algo;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConsistentHash<T> {

    private final HashFunc hashFunc;
    private final int numberOfReplicas;
    private final ConcurrentSkipListMap<Long, T> circle = new ConcurrentSkipListMap<>();

    public ConsistentHash(HashFunc hashFunc,
                          Collection<T> nodes,
                          int numberOfReplicas) {
        this.hashFunc = hashFunc;
        this.numberOfReplicas = numberOfReplicas;

        for (T node : nodes) {
            add(node);
        }
    }

    /**
     * @param node
     */
    public void add(T node) {
        String pattern = node.toString();
        hashFunc.seed(pattern);

        for (int i = 0; i < numberOfReplicas; i++) {
//            long hash = hashFunc.hash(pattern + i); // 是否 "+ i", 端視妳的 HashFunc 是否可設定 seed
            long hash = hashFunc.hash(pattern);
            if (! circle.containsKey(hash)) {
                circle.put(hash, node);
            }
        }
    }

    public void remove(T node) {
        String pattern = node.toString();
        hashFunc.seed(pattern);

        for (int i = 0; i < numberOfReplicas; i++) {
//            long hash = hashFunc.hash(pattern + i); // 是否 "+ i", 端視妳的 HashFunc 是否可設定 seed
            long hash = hashFunc.hash(pattern);
            T circleNode = circle.get(hash);
            if (Objects.nonNull(circleNode) && circleNode.toString().equals(pattern)) {
                circle.remove(hash);
            }
        }
    }

    public T get(String key) {
        if (circle.isEmpty()) {
            return null;
        }

        long hash = hashFunc.hash(key);
        if (! circle.containsKey(hash)) {
            // TODO what's the performance of tailMap?
            ConcurrentNavigableMap<Long, T> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }

        return circle.get(hash);
    }

    public Integer getNumberOfReplicas() {
        return numberOfReplicas;
    }

    public Map<Long, String> getCircleRecords() {
        Map<Long, String> records = new HashMap<>();
        for (Map.Entry<Long, T> shard : circle.entrySet()) {
            Long hash = shard.getKey();
            T node = shard.getValue();
            records.put(hash, node.toString());
        }

        return records;
    }
}
