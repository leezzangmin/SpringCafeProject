package com.zzangmin.gesipan.component.caching.redis;

import org.springframework.data.redis.core.ScanOptions;

public class RedisKeyUtils {

    public static final long CLIENT_ADDRESS_POST_REQUEST_WRITE_EXPIRE_DURATION_SEC = 86400;
    public static final String SCHEDULE_HITCOUNT_KEY = "scheduleHitCounts";
    public static final long SCHEDULED_INCREASE_SECONDS = 10;

    public static final ScanOptions SCHEDULE_HITCOUNT_SCAN_OPTION = ScanOptions.scanOptions()
        .match(RedisKeyUtils.generateScanKey(RedisKeyUtils.SCHEDULE_HITCOUNT_KEY))
        .count(100)
        .build();

    private RedisKeyUtils() {
        throw new AssertionError();
    }

    public static String generateScanKey(String key) {
        return key + ":*";
    }

    public static Long extractPostIdFromHitCountKey(String key) {
        return Long.valueOf(key.split(":")[1]);
    }

}
