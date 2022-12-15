package com.zzangmin.gesipan.component.caching.redis;

import com.google.common.base.Charsets;
import com.zzangmin.gesipan.component.basiccrud.repository.PostRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisScheduleService {

    private final RedisTemplate<String, String> redisTemplate;
    private final PostRepository postRepository;

    @Transactional
    @Scheduled(fixedRate = RedisKeyUtils.SCHEDULED_INCREASE_SECONDS, timeUnit = TimeUnit.SECONDS)
    public void scheduledIncreasePostHitCounts() {
        log.debug("scheduled task(hitCount) time: {}", LocalDateTime.now());

        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory()
            .getConnection()
            .scan(RedisKeyUtils.SCHEDULE_HITCOUNT_SCAN_OPTION);

        Map<Long, Long> postIdsAndHitCounts = extractPostIdsAndHitCountsFromCursor(cursor);

        hitCountBulkUpdate(postIdsAndHitCounts);
    }

    private Map<Long, Long> extractPostIdsAndHitCountsFromCursor(Cursor<byte[]> cursor) {
        Map<Long, Long> postIdsAndHitCounts = new HashMap<>();
        while (cursor.hasNext()) {
            String key = new String(cursor.next(), Charsets.UTF_8);
            Long postId = RedisKeyUtils.extractPostIdFromHitCountKey(key);
            String value = redisTemplate.opsForValue().getAndDelete(key);
            postIdsAndHitCounts.put(postId, Long.valueOf(value));
        }
        return postIdsAndHitCounts;
    }

    private void hitCountBulkUpdate(Map<Long, Long> postIdsAndHitCounts) {
        for (Long postId : postIdsAndHitCounts.keySet()) {
            postRepository.updateHitCountByPostId(postId, postIdsAndHitCounts.get(postId));
        }
    }

}
