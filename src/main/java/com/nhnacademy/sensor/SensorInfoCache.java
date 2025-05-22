package com.nhnacademy.sensor;

import com.nhnacademy.sensor.dto.SensorDataIndex;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SensorInfoCache {

    private final Set<SensorDataIndex> cache = ConcurrentHashMap.newKeySet();

    private final Set<SensorDataIndex> task = ConcurrentHashMap.newKeySet();

    public boolean contains(SensorDataIndex index) {
        return cache.contains(index);
    }

    public void add(SensorDataIndex index) {
        cache.add(index);
    }

    public boolean markTaskIfAbsent(SensorDataIndex index) {
        return task.add(index);
    }

    public void unmarkTask(SensorDataIndex index) {
        task.remove(index);
    }

    public void refresh(Set<SensorDataIndex> newCache) {
        cache.clear();
        cache.addAll(newCache);
    }
}
