package com.zzangmin.gesipan.component.caching.redis;

import com.google.common.base.Charsets;
import com.zzangmin.gesipan.component.basiccrud.repository.jdbc.PostJdbcRepository;
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
    private final PostJdbcRepository postJdbcRepository;

    @Transactional
    @Scheduled(fixedRate = RedisKeyUtils.SCHEDULED_INCREASE_SECONDS, timeUnit = TimeUnit.SECONDS)
    public void scheduledIncreasePostHitCounts() {
        log.debug("scheduled task(hitCount) time: {}", LocalDateTime.now());

        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory()
            .getConnection()
            .scan(RedisKeyUtils.SCHEDULE_HITCOUNT_SCAN_OPTION);

        String updateRows = generateRowConstructorUpdateString(cursor);
        if (updateRows.isBlank()) {
            return;
        }

        postJdbcRepository.hitCount(updateRows);
    }

    private String generateRowConstructorUpdateString(Cursor<byte[]> cursor) {
        StringBuilder sb = new StringBuilder();
        while (cursor.hasNext()) {
            sb.append("ROW(");
            String key = new String(cursor.next(), Charsets.UTF_8);
            sb.append(key);
            sb.append(",");
            String value = redisTemplate.opsForValue().getAndDelete(key);
            sb.append(value);
            sb.append("),");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
        }

        return sb.toString();
    }
}
