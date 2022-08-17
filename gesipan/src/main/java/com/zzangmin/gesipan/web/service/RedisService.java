package com.zzangmin.gesipan.web.service;

import com.zzangmin.gesipan.dao.PostRepository;
import com.zzangmin.gesipan.web.entity.Post;
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


// memory 관리 해야함 -> maxmemory 설정이 있는데 있어도 더 사용할 가능성 있음. - RSS값 모니터링 - 나도 모르는 새 swap이 발생할 수도 있음
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


    // 작동 안됨 - 트랜잭션 비활성화
    // @PreDestroy
    public void doRestTask() {

        System.out.println("RedisService.doRestTask");
        scheduledIncreasePostHitCounts();

        log.info("redis cache cleanup complete");
    }

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

        List<Post> posts = postRepository.findAllById(postIds);

        IntStream.range(0, posts.size())
                .boxed()
                .forEach(i -> postRepository.updateHitCountByPostId(posts.get(i).getPostId(), hitCounts.get(i)));

        postIds.stream()
                .forEach(i -> hashOperations.delete(scheduleHitCountHashKey, i));
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
        // 컬렉션에 담으면 item 개별로 expire 불가능 -> 컬렉션 전체에 대해서 expire만 가능
    }

    // key 형식 : 'client Address + postId' ->  '127.0.0.1:500'
    // TODO : ip -> JWT로 구분하기
    private String generateKey(String clientAddress, Long postId) {
        return clientAddress + ":" + postId;
    }
}
