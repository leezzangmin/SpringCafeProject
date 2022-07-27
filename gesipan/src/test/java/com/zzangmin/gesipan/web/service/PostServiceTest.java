package com.zzangmin.gesipan.web.service;

import com.zzangmin.gesipan.dao.PostCategoryRepository;
import com.zzangmin.gesipan.dao.PostRecommendRepository;
import com.zzangmin.gesipan.dao.PostRepository;
import com.zzangmin.gesipan.dao.UserRepository;
import com.zzangmin.gesipan.web.dto.post.PostResponse;
import com.zzangmin.gesipan.web.dto.post.PostSaveRequest;
import com.zzangmin.gesipan.web.dto.post.PostUpdateRequest;
import com.zzangmin.gesipan.web.entity.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
서
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostCategoryRepository postCategoryRepository;
    @Mock
    private CommentService commentService;
    @Mock
    private PostRecommendRepository postRecommendRepository;
    @Mock
    private RedisService redisService;

    @InjectMocks
    private PostService postService;

    @Test
    void findOne() {
        //given
        Long postId = 1L;
        Post post = Post.builder()
                .postId(1L)
                .postSubject("제목")
                .postContent("내용")
                .user(new Users())
                .postCategory(new PostCategory())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .hitCount(0L)
                .build();
        PostResponse postResponse1 = PostResponse.of(post, new ArrayList<>(), 0);
        when(postRepository.findByIdWithUser(postId)).thenReturn(Optional.of(post));
        when(commentService.findByPostId(postId)).thenReturn(List.of());
        when(postRecommendRepository.countByPostId(postId)).thenReturn(0);
        when(redisService.isFirstIpRequest(anyString(),anyLong())).thenReturn(true);

        //when
        PostResponse postResponse2 = postService.findOne(postId, "123.123.123.123:8080");
        //then
        Assertions.assertThat(postResponse1.getPostId()).isEqualTo(postResponse2.getPostId());
        Assertions.assertThat(postResponse1.getContent()).isEqualTo(postResponse2.getContent());
        Assertions.assertThat(postResponse2.getHitCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Post가 저장되어야 한다.")
    void save() {
        //given
        PostSaveRequest postSaveRequest = new PostSaveRequest("test제목1", "test내용1",1L, 1L, LocalDateTime.now());
        PostCategory postCategory = PostCategory.builder()
                .postCategoryId(1L)
                .categoryName(Categories.자유)
                .build();
        Users user = Users.builder()
                .userId(1L)
                .userEmail("ckdals1234@naver.com")
                .userName("이창민")
                .userNickname("zzangmin")
                .userRole(UserRole.일반)
                .createdAt(LocalDateTime.of(2022,2,2,2,2))
                .updatedAt(LocalDateTime.of(2022,2,2,2,2))
                .build();
        Post post = Post.builder()
                .postId(1L)
                .postSubject(postSaveRequest.getPostSubject())
                .postContent(postSaveRequest.getPostContent())
                .user(user)
                .postCategory(postCategory)
                .createdAt(postSaveRequest.getCreatedAt())
                .updatedAt(postSaveRequest.getCreatedAt())
                .build();
        when(postRepository.save(any())).thenReturn(post);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(postCategoryRepository.findById(any())).thenReturn(Optional.of(postCategory));
        //when
        Long saveId = postService.save(postSaveRequest);
        //then
        Assertions.assertThat(saveId).isEqualTo(1L);
    }

    @Test
    @DisplayName("Post가 삭제되어야 한다.")
    void delete() {
        //given
        Post post = Post.builder()
                .postId(1L)
                .postSubject("delete제목")
                .postContent("delete내용")
                .user(new Users())
                .postCategory(new PostCategory())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.findById(2L)).thenThrow(new IllegalStateException());
        //when
        postService.delete(1L);
        when(postRepository.findById(1L)).thenThrow();
        //then
        Assertions.assertThatThrownBy(() -> postService.delete(1L));
        Assertions.assertThatThrownBy(() -> postService.delete(2L));
    }

    @Test
    @DisplayName("post가 업데이트 되어야 한다.")
    void update() {
        //given
        PostCategory postCategory = PostCategory.builder()
                .postCategoryId(1L)
                .categoryName(Categories.자유)
                .build();
        Users user = Users.builder()
                .userId(1L)
                .userEmail("ckdals1234@naver.com")
                .userName("이창민")
                .userNickname("zzangmin")
                .userRole(UserRole.일반)
                .createdAt(LocalDateTime.of(2022,2,2,2,2))
                .updatedAt(LocalDateTime.of(2022,2,2,2,2))
                .build();
        Post post = Post.builder()
                .postId(1L)
                .postSubject("update제목")
                .postContent("update내용")
                .user(user)
                .postCategory(postCategory)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        LocalDateTime updateTime = LocalDateTime.parse("2022-07-04T12:39:00");
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("수정제목", "수정내용", updateTime);
        //when
        postService.update(1L, postUpdateRequest);
        //then
        Assertions.assertThat(post.getPostSubject()).isEqualTo("수정제목");
        Assertions.assertThat(post.getPostContent()).isEqualTo("수정내용");
        Assertions.assertThat(post.getUpdatedAt()).isEqualTo(updateTime);
    }
}