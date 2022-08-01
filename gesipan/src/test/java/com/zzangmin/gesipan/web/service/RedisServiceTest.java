package com.zzangmin.gesipan.web.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
class RedisServiceTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisService redisService;

    // 레디스는 롤백이 없다.
    // 초기상태로 만드는 메서드를 만들어서 사용해야한다 ?
    @BeforeEach
    private void deleteAll() {
        stringRedisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("첫번째 게시글 요청 후에는 방문기록이 남아야 한다.")
    void 첫번째요청방문기록() {
        //given

        //when
        redisService.increasePostHitCount("123.123.123.123", 444L);
        //then
        Assertions.assertThat(stringRedisTemplate.hasKey("123.123.123.123:444")).isTrue();
        Assertions.assertThat(stringRedisTemplate.hasKey("scheduleHitCount:444")).isTrue();

    }

    @Test
    @DisplayName("두번째 중복된 게시글 요청 후에는 스케줄링 목록값이 1이여야 한다.")
    void 두번째요청방문기록() {
        //given
        //when
        redisService.increasePostHitCount("123.123.123.123", 444L);
        redisService.increasePostHitCount("123.123.123.123", 444L);
        //then
        Assertions.assertThat(stringRedisTemplate.opsForValue().get("scheduleHitCount:444")).isEqualTo("1");
    }

    @Test
    @DisplayName("두번째 중복되지 않은 게시글 요청 후에는 스케줄링 목록값이 2이여야 한다.")
    void 두번째요청방문기록_2() {
        //given

        //when
        redisService.increasePostHitCount("123.123.123.123", 444L);
        redisService.increasePostHitCount("000.000.000.000", 444L);
        //then
        Assertions.assertThat(stringRedisTemplate.opsForValue().get("scheduleHitCount:444")).isEqualTo("2");
        Set<String> keys = stringRedisTemplate.keys("*");
    }

    @Test
    void scheduledIncreasePostHitCounts() {
    }
}