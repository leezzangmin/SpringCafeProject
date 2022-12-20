package com.zzangmin.gesipan.component.basiccrud.service;

import static org.assertj.core.api.Assertions.within;

import com.zzangmin.gesipan.component.basiccrud.dto.temporarypost.TemporaryPostSaveRequest;
import com.zzangmin.gesipan.component.basiccrud.entity.TemporaryPost;
import com.zzangmin.gesipan.component.basiccrud.repository.PostCategoryRepository;
import com.zzangmin.gesipan.component.basiccrud.repository.PostRepository;
import com.zzangmin.gesipan.component.basiccrud.repository.TemporaryPostRepository;
import com.zzangmin.gesipan.component.login.entity.Users;
import com.zzangmin.gesipan.component.login.repository.UsersRepository;
import com.zzangmin.gesipan.testfactory.EntityFactory;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class TemporaryPostServiceTest {

    @Autowired
    TemporaryPostService temporaryPostService;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    TemporaryPostRepository temporaryPostRepository;
    @Autowired
    PostService postService;
    @Autowired
    PostCategoryRepository postCategoryRepository;
    @Autowired
    PostRepository postRepository;

    @DisplayName("존재하지 않는 userId로 임시게시물 저장을 요청하면 오류가 발생해야 한다.")
    @Test
    void postTemporarySave_invalidUserId() {
        //given
        Long invalidUserId=99999999999999L;
        TemporaryPostSaveRequest temporaryPostSaveRequest = new TemporaryPostSaveRequest("post_subject", "post_content", 1L, LocalDateTime.now());
        //when
        //then
        Assertions.assertThatThrownBy(() -> temporaryPostService.postTemporarySave(invalidUserId, temporaryPostSaveRequest));
    }

    @DisplayName("임시게시물 저장을 요청하면 저장되어야 한다.")
    @Test
    void postTemporarySave() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        TemporaryPostSaveRequest temporaryPostSaveRequest = new TemporaryPostSaveRequest("post_subject", "post_content", 1L, LocalDateTime.now());
        usersRepository.save(user);
        //when
        temporaryPostService.postTemporarySave(user.getUserId(), temporaryPostSaveRequest);
        //then
        TemporaryPost findPost = temporaryPostRepository.findByUserId(user.getUserId()).get(0);
        Assertions.assertThat(findPost.getPostSubject()).isEqualTo(temporaryPostSaveRequest.getPostSubject());
        Assertions.assertThat(findPost.getPostContent()).isEqualTo(temporaryPostSaveRequest.getPostContent());
        Assertions.assertThat(findPost.getCreatedAt()).isCloseTo(temporaryPostSaveRequest.getCreatedAt(), within(1,ChronoUnit.SECONDS));
    }

    @DisplayName("존재하지 않는 임시게시글을 삭제하려고 하면 오류가 발생해야 한다.")
    @Test
    void postTemporaryDelete_noExistId() {
        //given
        Long invalidTemporaryPostId = 123123123L;
        //when //then
        Assertions.assertThatThrownBy(() ->temporaryPostService.postTemporaryDelete(12345L, invalidTemporaryPostId))
                .isInstanceOf(IllegalArgumentException.class);

    }

}
