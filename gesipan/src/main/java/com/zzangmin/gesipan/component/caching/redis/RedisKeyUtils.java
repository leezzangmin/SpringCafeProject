package com.zzangmin.gesipan.component.caching.redis;

public class RedisKeyUtils {

    public static final long clientAddressPostRequestWriteExpireDurationSec = 86400;
    public static final String scheduleHitCountKey = "scheduleHitCounts";
    public static final long scheduledIncreaseSeconds = 10;



    private RedisKeyUtils() {
        throw new AssertionError();
    }
}
