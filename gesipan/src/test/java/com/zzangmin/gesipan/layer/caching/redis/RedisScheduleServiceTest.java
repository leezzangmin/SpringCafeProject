package com.zzangmin.gesipan.layer.caching.redis;

import com.zzangmin.gesipan.layer.basiccrud.entity.Categories;
import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import com.zzangmin.gesipan.layer.basiccrud.entity.PostCategory;
import com.zzangmin.gesipan.layer.basiccrud.repository.PostCategoryRepository;
import com.zzangmin.gesipan.layer.basiccrud.repository.PostRepository;
import com.zzangmin.gesipan.layer.embeddable.BaseTime;
import com.zzangmin.gesipan.layer.login.entity.UserRole;
import com.zzangmin.gesipan.layer.login.entity.Users;
import com.zzangmin.gesipan.layer.login.repository.UsersRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
class RedisScheduleServiceTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCategoryRepository postCategoryRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RedisScheduleService redisScheduleService;

    @BeforeEach
    private void deleteAll() {
        redisTemplate.getConnectionFactory()
                .getConnection()
                .flushAll();
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
                .baseTime(new BaseTime(LocalDateTime.of(2022,2,2,2,2), LocalDateTime.of(2022,2,2,2,2)))
                .build();
        Post post = Post.builder()
                .postSubject("가짜제목")
                .postContent("가짜내용")
                .user(user)
                .postCategory(postCategory)
                .baseTime(new BaseTime(LocalDateTime.now(), LocalDateTime.now()))
                .hitCount(0L)
                .build();

        postCategoryRepository.save(postCategory);
        usersRepository.save(user);
        Post savedPost = postRepository.save(post);
        redisTemplate.opsForValue()
                .increment("scheduleHitCounts:" + savedPost.getPostId().toString(), 15);

        //when
        redisScheduleService.scheduledIncreasePostHitCounts();

        //then
        Post post1 = postRepository.findById(savedPost.getPostId()).get();
        Assertions.assertThat(savedPost.getHitCount() + 15)
                .isEqualTo(post1.getHitCount());

        String scheduleHitCounts = redisTemplate.opsForValue().get("scheduleHitCounts:" + savedPost.getPostId().toString());
        Assertions.assertThat(scheduleHitCounts).isNull();
    }
}