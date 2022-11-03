package com.zzangmin.gesipan.layer.basiccrud.service;

import com.zzangmin.gesipan.layer.basiccrud.entity.*;
import com.zzangmin.gesipan.layer.basiccrud.repository.PostCategoryRepository;
import com.zzangmin.gesipan.layer.basiccrud.repository.PostRepository;
import com.zzangmin.gesipan.layer.basiccrud.repository.TemporaryPostRepository;
import com.zzangmin.gesipan.layer.login.entity.UserRole;
import com.zzangmin.gesipan.layer.login.entity.Users;
import com.zzangmin.gesipan.layer.login.repository.UsersRepository;
import com.zzangmin.gesipan.layer.basiccrud.service.PostService;
import com.zzangmin.gesipan.layer.basiccrud.dto.post.PostResponse;
import com.zzangmin.gesipan.layer.basiccrud.dto.post.PostSaveRequest;
import com.zzangmin.gesipan.layer.basiccrud.dto.post.PostUpdateRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Autowired private PostRepository postRepository;
    @Autowired private UsersRepository usersRepository;
    @Autowired private PostCategoryRepository postCategoryRepository;
    @Autowired private TemporaryPostRepository temporaryPostRepository;
    @Autowired private PostService postService;

    @Test
    void findOne() {
        //given
        PostCategory postCategory = PostCategory.builder()
                .categoryName(Categories.FREE)
                .build();
        Users user = Users.builder()
                .userEmail("ckdals1234@naver.com")
                .userName("이창민")
                .userNickname("zzangmin")
                .userRole(UserRole.NORMAL)
                .createdAt(LocalDateTime.of(2022,2,2,2,2))
                .updatedAt(LocalDateTime.of(2022,2,2,2,2))
                .build();
        Post post = Post.builder()
                .postSubject("제목")
                .postContent("내용")
                .user(user)
                .postCategory(postCategory)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .hitCount(0L)
                .build();
        Long postCategoryId = postCategoryRepository.save(postCategory).getPostCategoryId();
        Long userId = usersRepository.save(user).getUserId();
        Long postId = postRepository.save(post).getPostId();
        //when
        PostResponse postResponse = postService.findOne(postId, Optional.empty());
        //then
        Assertions.assertThat(postResponse.getPostId()).isEqualTo(post.getPostId());
        Assertions.assertThat(postResponse.getPostContent()).isEqualTo(post.getPostContent());
    }

    @Test
    @DisplayName("Post가 저장되어야 한다.")
    void save() {
        //given
        PostCategory postCategory = PostCategory.builder()
                .categoryName(Categories.FREE)
                .build();
        Long postCategoryId = postCategoryRepository.save(postCategory).getPostCategoryId();
        PostSaveRequest postSaveRequest = new PostSaveRequest("test제목1", "test내용1", postCategoryId, LocalDateTime.now(), null);
        Users user = Users.builder()
                .userEmail("ckdals1234@naver.com")
                .userName("이창민")
                .userNickname("zzangmin")
                .userRole(UserRole.NORMAL)
                .createdAt(LocalDateTime.of(2022,2,2,2,2))
                .updatedAt(LocalDateTime.of(2022,2,2,2,2))
                .build();

        Long userId = usersRepository.save(user).getUserId();
        //when
        Long saveId = postService.save(userId, postSaveRequest);
        //then
        Post findPost = postRepository.findById(saveId).get();
        Assertions.assertThat(findPost.getPostSubject()).isEqualTo(postSaveRequest.getPostSubject());
    }

    @Test
    @DisplayName("임시 게시물을 불러와서 저장하면 임시 게시물은 삭제되어야 한다.")
    void save2() {
        //given
        PostCategory postCategory = PostCategory.builder()
                .categoryName(Categories.FREE)
                .build();
        Users user = Users.builder()
                .userEmail("ckdals1234@naver.com")
                .userName("이창민")
                .userNickname("zzangmin")
                .userRole(UserRole.NORMAL)
                .createdAt(LocalDateTime.of(2022,2,2,2,2))
                .updatedAt(LocalDateTime.of(2022,2,2,2,2))
                .build();
        TemporaryPost temporaryPost = TemporaryPost.builder()
                .postSubject("temp제목")
                .postContent("temp내용")
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();


        Long postCategoryId = postCategoryRepository.save(postCategory).getPostCategoryId();
        Long userId = usersRepository.save(user).getUserId();
        Long tempPostId = temporaryPostRepository.save(temporaryPost).getTempPostId();
        PostSaveRequest postSaveRequest = new PostSaveRequest("test제목1", "test내용1",postCategoryId, LocalDateTime.now(), tempPostId);
        //when
        Long savedPostId = postService.save(userId, postSaveRequest);
        //then
        List<TemporaryPost> temporaryPosts = temporaryPostRepository.findByUserId(1L);
        Assertions.assertThat(temporaryPosts.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Post가 삭제되어야 한다.")
    void delete() {
        //given
        PostCategory postCategory = PostCategory.builder()
                .categoryName(Categories.FREE)
                .build();
        Users user = Users.builder()
                .userEmail("ckdals1234@naver.com")
                .userName("이창민")
                .userNickname("zzangmin")
                .userRole(UserRole.NORMAL)
                .createdAt(LocalDateTime.of(2022,2,2,2,2))
                .updatedAt(LocalDateTime.of(2022,2,2,2,2))
                .build();
        Post post = Post.builder()
                .postSubject("delete제목")
                .postContent("delete내용")
                .user(user)
                .postCategory(postCategory)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Long postCategoryId = postCategoryRepository.save(postCategory).getPostCategoryId();
        Long userId = usersRepository.save(user).getUserId();
        Long postId = postRepository.save(post).getPostId();
        //when
        postService.delete(postId, userId);
        //then
        Assertions.assertThatThrownBy(() -> postService.delete(postId, 100000L));
        Assertions.assertThatThrownBy(() -> postRepository.findById(postId).get());

    }

    @Test
    @DisplayName("post가 업데이트 되어야 한다.")
    void update() {
        //given
        PostCategory postCategory = PostCategory.builder()
                .categoryName(Categories.FREE)
                .build();
        Users user = Users.builder()
                .userEmail("ckdals1234@naver.com")
                .userName("이창민")
                .userNickname("zzangmin")
                .userRole(UserRole.NORMAL)
                .createdAt(LocalDateTime.of(2022,2,2,2,2))
                .updatedAt(LocalDateTime.of(2022,2,2,2,2))
                .build();
        Post post = Post.builder()
                .postSubject("update제목")
                .postContent("update내용")
                .user(user)
                .postCategory(postCategory)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Long postCategoryId = postCategoryRepository.save(postCategory).getPostCategoryId();
        Long userId = usersRepository.save(user).getUserId();
        Long postId = postRepository.save(post).getPostId();

        LocalDateTime updateTime = LocalDateTime.parse("2022-07-04T12:39:00");
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("수정제목", "수정내용", updateTime);
        //when
        postService.update(postId, postUpdateRequest, userId);
        //then
        Post findPost = postRepository.findById(postId).get();
        Assertions.assertThat(findPost.getPostSubject()).isEqualTo("수정제목");
        Assertions.assertThat(findPost.getPostContent()).isEqualTo("수정내용");
        Assertions.assertThat(findPost.getUpdatedAt()).isEqualTo(updateTime);
    }
}
