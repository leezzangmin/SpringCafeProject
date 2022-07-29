package com.zzangmin.gesipan.web.service;

import com.zzangmin.gesipan.dao.PostRepository;
import com.zzangmin.gesipan.web.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {
    private final long clientAddressPostRequestWriteExpireDurationSec = 86400;
    private final long scheduledIncreaseSeconds = 10;
    private final String scheduleHitCountKeyPrefix = "scheduleHitCount:";

    private final StringRedisTemplate redisTemplate;
    private final PostRepository postRepository;


    public void increasePostHitCount(String clientAddress, Long postId) {
        if (isFirstIpRequest(clientAddress, postId)) {
            insertHitCountsToSchedulingList(postId);
            cacheClientRequest(clientAddress, postId);
        }
        log.debug("same user requests duplicate in 24hours: {}, {}", clientAddress, postId);
    }

    /**
     * fixedRate, fixedDelay 차이점
     *
     *  fixedRate는 작업 수행시간과 상관없이 일정 주기마다 메소드를 호출하는 것이고,
     * fixedDelay는 (작업 수행 시간을 포함하여) 작업을 마친 후부터 주기 타이머가 돌아 메소드를 호출하는 것이다.
     */
    @Scheduled(fixedRate = scheduledIncreaseSeconds, timeUnit = TimeUnit.SECONDS)
    @Transactional
    public void scheduledIncreasePostHitCounts() {
        log.debug("스케줄 태스크 {}", LocalDateTime.now());

        ScanOptions build = ScanOptions.scanOptions().match(scheduleHitCountKeyPrefix + "*").build();

        List<String> keys = redisTemplate.scan(build)
                .stream()
                .collect(Collectors.toList());

        List<Long> hitCounts = redisTemplate.opsForValue()
                .multiGet(keys)
                .stream()
                .map(i -> Long.valueOf(i))
                .collect(Collectors.toList());
        List<Long> postIds = keys.stream()
                .map(i -> Long.valueOf(i.split(":")[1]))
                .collect(Collectors.toList());

        List<Post> posts = postRepository.findAllById(postIds);
        IntStream.range(0, posts.size()).boxed()
                        .forEach(i -> posts.get(i).increaseHitCount(hitCounts.get(i)));

        // TODO: 이거 고치기 -> 어떻게 하는거야
        // postRepository.updatePostsHitCounts(postIds, hitCounts);
        redisTemplate.delete(keys);
    }

    // 조회수 스케줄링에 사용할 키 형식 -> "scheduleHitCount:+postId" -> scheduleHitCount:1234
    private void insertHitCountsToSchedulingList(Long postId) {
        redisTemplate.opsForValue().increment(scheduleHitCountKeyPrefix + postId);
    }

    // https://devlog-wjdrbs96.tistory.com/375
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
        redisTemplate.opsForValue().set(key, "", clientAddressPostRequestWriteExpireDurationSec, TimeUnit.SECONDS);
    }

    // key 형식 : 'client Address + postId' ->  '127.0.0.1:500'
    private String generateKey(String clientAddress, Long postId) {
        return clientAddress + ":" + postId;
    }
}
