package com.zzangmin.gesipan.web.service;

import com.zzangmin.gesipan.dao.PostCategoryRepository;
import com.zzangmin.gesipan.dao.PostRepository;
import com.zzangmin.gesipan.dao.UserRepository;
import com.zzangmin.gesipan.web.dto.PostSaveRequest;
import com.zzangmin.gesipan.web.dto.PostUpdateRequest;
import com.zzangmin.gesipan.web.entity.Post;
import com.zzangmin.gesipan.web.entity.PostCategory;
import com.zzangmin.gesipan.web.entity.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @InjectMocks
    private PostService postService;

    @Test
    void findOne() {
    }

    @Test
    @DisplayName("Post가 저장되어야 한다.")
    void save() {
        //given
        PostSaveRequest postSaveRequest = new PostSaveRequest("test제목1", "test내용1",1L, 1L, LocalDateTime.now());
        Users user = new Users();
        PostCategory postCategory = new PostCategory();
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
        Post post = Post.builder()
                .postId(1L)
                .postSubject("update제목")
                .postContent("update내용")
                .user(new Users())
                .postCategory(new PostCategory())
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