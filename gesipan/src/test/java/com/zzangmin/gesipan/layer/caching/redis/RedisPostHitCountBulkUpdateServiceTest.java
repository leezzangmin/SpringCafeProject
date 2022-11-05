package com.zzangmin.gesipan.layer.caching.redis;

import com.zzangmin.gesipan.layer.basiccrud.repository.PostCategoryRepository;
import com.zzangmin.gesipan.layer.basiccrud.repository.PostRepository;
import com.zzangmin.gesipan.layer.login.repository.UsersRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
class RedisPostHitCountBulkUpdateServiceTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RedisPostHitCountBulkUpdateService redisPostHitCountBulkUpdateService;

    @BeforeEach
    private void deleteAll() {
        redisTemplate.getConnectionFactory()
                .getConnection()
                .flushAll();
    }

    @Test
    @DisplayName("첫번째 게시글 요청 후에는 방문기록이 남아야 한다.")
    void 첫번째요청방문기록() {
        //given
        //when
        redisPostHitCountBulkUpdateService.increasePostHitCount("123.123.123.123", 444L);
        //then
        Assertions.assertThat(redisTemplate.opsForValue().get("scheduleHitCounts:444")).isEqualTo("1");
        Assertions.assertThat(redisTemplate.hasKey("123.123.123.123:444")).isTrue();
    }

    @Test
    @DisplayName("두번째 중복된 게시글 요청 후에는 스케줄링 목록값이 1이여야 한다.")
    void 두번째요청방문기록() {
        //given
        //when
        redisPostHitCountBulkUpdateService.increasePostHitCount("123.123.123.123", 444L);
        redisPostHitCountBulkUpdateService.increasePostHitCount("123.123.123.123", 444L);
        //then
        Assertions.assertThat(redisTemplate.opsForValue().get("scheduleHitCounts:444")).isEqualTo("1");
    }

    @Test
    @DisplayName("두번째 중복되지 않은 게시글 요청 후에는 스케줄링 목록값이 2이여야 한다.")
    void 두번째요청방문기록_2() {
        //given

        //when
        redisPostHitCountBulkUpdateService.increasePostHitCount("123.123.123.123", 444L);
        redisPostHitCountBulkUpdateService.increasePostHitCount("000.000.000.000", 444L);
        //then
        Assertions.assertThat(redisTemplate.opsForValue().get("scheduleHitCounts:444")).isEqualTo("2");
    }

    @Test
    @DisplayName("중복방지를 위해 ip로 게시글을 방문한 기록이 유지되어야 한다.")
    void 중복ip기록() {
        //given

        //when
        redisPostHitCountBulkUpdateService.increasePostHitCount("123.123.123.123",123L);
        //then
        String record = redisTemplate.opsForValue()
                .get("123.123.123.123:123").toString();
        Assertions.assertThat(record).isNotNull();
    }


}
