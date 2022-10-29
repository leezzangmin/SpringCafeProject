package com.zzangmin.gesipan.layer.caching.redis;

import com.zzangmin.gesipan.layer.basiccrud.repository.PostRepository;
import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {

    private final long clientAddressPostRequestWriteExpireDurationSec = 86400;
    private final long scheduledIncreaseSeconds = 10;
    private final String scheduleHitCountHashKey = "scheduleHitCounts";

    private final RedisTemplate redisTemplate;
    private final PostRepository postRepository;

    private final ScanOptions scanOptions = ScanOptions.scanOptions()
            .count(100)
            .build();


    public void increasePostHitCount(String clientAddress, Long postId) {
        if (isFirstIpRequest(clientAddress, postId)) {
            insertHitCountsToSchedulingList(postId);
            cacheClientRequest(clientAddress, postId);
        }
        log.debug("same user requests duplicate in 24hours: {}, {}", clientAddress, postId);
    }

    @Transactional
    @Scheduled(fixedRate = scheduledIncreaseSeconds, timeUnit = TimeUnit.SECONDS)
    public void scheduledIncreasePostHitCounts() {
        log.debug("스케줄 태스크 {}", LocalDateTime.now());

        HashOperations<String, Long, Long> hashOperations = redisTemplate.opsForHash();
        List<Long> postIds = new ArrayList<>();
        List<Long> hitCounts = new ArrayList<>();

        Cursor<Map.Entry<Long, Long>> cursor = hashOperations.scan(scheduleHitCountHashKey, scanOptions);
        while (cursor.hasNext()) {
            Map.Entry<Long, Long> next = cursor.next();
            postIds.add(next.getKey());
            hitCounts.add(next.getValue());
        }

        hitCountBulkUpdate(postIds, hitCounts);

        postIds.stream()
                .forEach(i -> hashOperations.delete(scheduleHitCountHashKey, i));
    }

    private void hitCountBulkUpdate(List<Long> postIds, List<Long> hitCounts) {
        List<Post> posts = postRepository.findAllById(postIds);
        IntStream.range(0, posts.size())
                .boxed()
                .forEach(i -> postRepository.updateHitCountByPostId(posts.get(i).getPostId(), hitCounts.get(i)));
    }


    private void insertHitCountsToSchedulingList(Long postId) {
        redisTemplate.opsForHash()
                        .increment(scheduleHitCountHashKey, postId, 1);
    }

    private boolean isFirstIpRequest(String clientAddress, Long postId) {
        String key = generateKey(clientAddress, postId);
        log.debug("user post request key: {}", key);
        if (redisTemplate.hasKey(key)) {
            return false;
        }
        return true;
    }

    private void cacheClientRequest(String clientAddress, Long postId) {
        String key = generateKey(clientAddress, postId);
        log.debug("user post request key: {}", key);
        redisTemplate.opsForValue()
                .set(key, "", clientAddressPostRequestWriteExpireDurationSec, TimeUnit.SECONDS);
    }

    private String generateKey(String clientAddress, Long postId) {
        return clientAddress + ":" + postId;
    }
}
