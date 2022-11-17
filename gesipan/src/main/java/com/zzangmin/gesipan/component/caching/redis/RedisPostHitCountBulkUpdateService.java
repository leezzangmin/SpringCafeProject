package com.zzangmin.gesipan.component.caching.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisPostHitCountBulkUpdateService {

    private final RedisTemplate<String, String> redisTemplate;

    public void increasePostHitCount(String clientAddress, Long postId) {
        if (isFirstIpRequest(clientAddress, postId)) {
            insertHitCountsToSchedulingList(postId);
            cacheClientRequest(clientAddress, postId);
        }
        log.debug("same user requests duplicate in 24hours: {}, {}", clientAddress, postId);
    }

    private void insertHitCountsToSchedulingList(Long postId) {
        redisTemplate.opsForValue()
                        .increment(generateScheduleKey(postId), 1);
    }

    private boolean isFirstIpRequest(String clientAddress, Long postId) {
        String key = generateCacheKey(clientAddress, postId);
        log.debug("user post request key: {}", key);
        if (redisTemplate.hasKey(key)) {
            return false;
        }
        return true;
    }

    private void cacheClientRequest(String clientAddress, Long postId) {
        String key = generateCacheKey(clientAddress, postId);
        log.debug("user post request key: {}", key);
        redisTemplate.opsForValue()
                .set(key, "", RedisKeyUtils.clientAddressPostRequestWriteExpireDurationSec, TimeUnit.SECONDS);
    }

    private String generateCacheKey(String clientAddress, Long postId) {
        return clientAddress + ":" + postId;
    }
    private String generateScheduleKey(Long postId) {
        return RedisKeyUtils.scheduleHitCountKey + ":" + postId;
    }
}
