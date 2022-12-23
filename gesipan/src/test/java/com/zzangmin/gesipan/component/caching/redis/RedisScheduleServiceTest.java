package com.zzangmin.gesipan.component.caching.redis;

import com.zzangmin.gesipan.component.basiccrud.entity.Post;
import com.zzangmin.gesipan.component.basiccrud.entity.PostCategory;
import com.zzangmin.gesipan.component.basiccrud.repository.PostCategoryRepository;
import com.zzangmin.gesipan.component.basiccrud.repository.PostRepository;
import com.zzangmin.gesipan.component.login.entity.Users;
import com.zzangmin.gesipan.component.login.repository.UsersRepository;

import com.zzangmin.gesipan.testfactory.EntityFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;

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
    @Disabled
    @DisplayName("해시에 들어있던 값들이 스케줄 잡이 끝나면 사라져있어야 한다. 조회수도 상승되어야 한다.")
    void scheduledIncreasePostHitCounts() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        PostCategory postCategory = post.getPostCategory();

        postCategoryRepository.save(postCategory);
        usersRepository.save(user);
        postRepository.save(post);
        redisTemplate.opsForValue()
                .increment("scheduleHitCounts:" + post.getPostId().toString(), 15);

        //when
        redisScheduleService.scheduledIncreasePostHitCounts();

        //then
        Post post1 = postRepository.findById(post.getPostId()).get();
        Assertions.assertThat(post.getHitCount() + 15)
                .isEqualTo(post1.getHitCount());

        String scheduleHitCounts = redisTemplate.opsForValue().get("scheduleHitCounts:" + post.getPostId().toString());
        Assertions.assertThat(scheduleHitCounts).isNull();
    }
}
