package com.zzangmin.gesipan.layer.caching.redis;

import com.google.common.base.Charsets;
import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import com.zzangmin.gesipan.layer.basiccrud.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisScheduleService {
    private final RedisTemplate<String, String> redisTemplate;
    private final PostRepository postRepository;

    @Transactional
    @Scheduled(fixedRate = RedisKeyUtils.scheduledIncreaseSeconds, timeUnit = TimeUnit.SECONDS)
    public void scheduledIncreasePostHitCounts() {
        log.debug("스케줄 태스크 {}", LocalDateTime.now());
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(RedisKeyUtils.scheduleHitCountKey + ":*")
                .count(100)
                .build();

        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(scanOptions);
        List<Long> postIds = new ArrayList<>();
        List<Long> hitCounts = new ArrayList<>();

        while (cursor.hasNext()) {
            String key = new String(cursor.next(), Charsets.UTF_8);
            Long postId = extractPostIdFromHitCountKey(key);
            String value = redisTemplate.opsForValue().getAndDelete(key);

            postIds.add(postId);
            hitCounts.add(Long.valueOf(value));
        }

        hitCountBulkUpdate(postIds, hitCounts);
    }

    private Long extractPostIdFromHitCountKey(String key) {
        return Long.valueOf(key.split(":")[1]);
    }

    private void hitCountBulkUpdate(List<Long> postIds, List<Long> hitCounts) {
        List<Post> posts = postRepository.findAllById(postIds);
        IntStream.range(0, posts.size())
                .boxed()
                .forEach(i -> postRepository.updateHitCountByPostId(posts.get(i).getPostId(), hitCounts.get(i)));
    }


}
