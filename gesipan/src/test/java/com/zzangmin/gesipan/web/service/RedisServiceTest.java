package com.zzangmin.gesipan.web.service;

import com.zzangmin.gesipan.dao.PostCategoryRepository;
import com.zzangmin.gesipan.dao.PostRepository;
import com.zzangmin.gesipan.dao.UsersRepository;
import com.zzangmin.gesipan.web.entity.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
class RedisServiceTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisService redisService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCategoryRepository postCategoryRepository;
    @Autowired
    private UsersRepository usersRepository;

    // 레디스는 롤백이 없다.
    // 초기상태로 만드는 메서드를 만들어서 사용해야한다 ?
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
        redisService.increasePostHitCount("123.123.123.123", 444L);
        //then
        Assertions.assertThat(redisTemplate.opsForHash().hasKey("scheduleHitCounts", 444L)).isTrue();
        Assertions.assertThat(redisTemplate.opsForHash().get("scheduleHitCounts",444L)).isEqualTo(1L);
        Assertions.assertThat(redisTemplate.hasKey("123.123.123.123:444")).isTrue();
    }

    @Test
    @DisplayName("두번째 중복된 게시글 요청 후에는 스케줄링 목록값이 1이여야 한다.")
    void 두번째요청방문기록() {
        //given
        //when
        redisService.increasePostHitCount("123.123.123.123", 444L);
        redisService.increasePostHitCount("123.123.123.123", 444L);
        //then
        Assertions.assertThat(redisTemplate.opsForHash().get("scheduleHitCounts",444L)).isEqualTo(1L);
    }

    @Test
    @DisplayName("두번째 중복되지 않은 게시글 요청 후에는 스케줄링 목록값이 2이여야 한다.")
    void 두번째요청방문기록_2() {
        //given

        //when
        redisService.increasePostHitCount("123.123.123.123", 444L);
        redisService.increasePostHitCount("000.000.000.000", 444L);
        //then
        Assertions.assertThat(redisTemplate.opsForHash().get("scheduleHitCounts",444L)).isEqualTo(2L);
    }

    @Test
    @DisplayName("해시에 들어있던 값들이 스케줄 잡이 끝나면 사라져있어야 한다. 조회수도 상승되어야 한다.")
    void scheduledIncreasePostHitCounts() {
        //given
        PostCategory postCategory = PostCategory.builder()
                .postCategoryId(1L)
                .categoryName(Categories.FREE)
                .build();
        Users user = Users.builder()
                .userId(1L)
                .userEmail("가짜이메일@naver.com")
                .userName("가짜이름")
                .userNickname("가짜닉네임")
                .userRole(UserRole.NORMAL)
                .createdAt(LocalDateTime.of(2022,2,2,2,2))
                .updatedAt(LocalDateTime.of(2022,2,2,2,2))
                .build();
        Post post = Post.builder()
                .postSubject("가짜제목")
                .postContent("가짜내용")
                .user(user)
                .postCategory(postCategory)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .hitCount(0L)
                .build();

        postCategoryRepository.save(postCategory);
        usersRepository.save(user);
        Post savedPost = postRepository.save(post);

        HashOperations<String, Long, Long> hashOperations = redisTemplate.opsForHash();
        hashOperations
                .increment("scheduleHitCounts", savedPost.getPostId(), 15);
        //when
        redisService.scheduledIncreasePostHitCounts();
        //then
        Post post1 = postRepository.findById(savedPost.getPostId()).get();
        Assertions.assertThat(savedPost.getHitCount() + 15)
                .isEqualTo(post1.getHitCount());

        Long scheduleHitCounts = hashOperations.get("scheduleHitCounts", savedPost.getPostId());
        Assertions.assertThat(scheduleHitCounts).isNull();
    }
}
