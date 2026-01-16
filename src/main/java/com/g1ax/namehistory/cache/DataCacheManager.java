package com.g1ax.namehistory.cache;

import com.g1ax.namehistory.data.PlayerData;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class DataCacheManager {
    private static final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private static long cacheDuration = TimeUnit.HOURS.toMillis(1);
    private static final int MAX_CACHE_SIZE = 500;

    public static void cachePlayerData(String username, PlayerData data) {
        if (cache.size() >= MAX_CACHE_SIZE) {
            evictOldest();
        }
        cache.put(username.toLowerCase(), new CacheEntry(data, System.currentTimeMillis()));
    }

    public static PlayerData getCachedData(String username) {
        CacheEntry entry = cache.get(username.toLowerCase());
        if (entry != null && isCacheValid(username)) {
            return entry.data;
        }
        return null;
    }

    public static boolean isCacheValid(String username) {
        CacheEntry entry = cache.get(username.toLowerCase());
        return entry != null && (System.currentTimeMillis() - entry.timestamp) < cacheDuration;
    }

    public static void clearCache() {
        cache.clear();
    }

    public static void setCacheDuration(int hours) {
        cacheDuration = TimeUnit.HOURS.toMillis(hours);
    }

    private static void evictOldest() {
        cache.entrySet().stream()
            .min((e1, e2) -> Long.compare(e1.getValue().timestamp, e2.getValue().timestamp))
            .ifPresent(entry -> cache.remove(entry.getKey()));
    }

    private static class CacheEntry {
        final PlayerData data;
        final long timestamp;

        CacheEntry(PlayerData data, long timestamp) {
            this.data = data;
            this.timestamp = timestamp;
        }
    }
}
