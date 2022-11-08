package com.zzangmin.gesipan.layer.basiccrud.service;

import com.zzangmin.gesipan.layer.basiccrud.dto.comment.CommentResponse;
import com.zzangmin.gesipan.layer.basiccrud.entity.Categories;
import com.zzangmin.gesipan.layer.basiccrud.entity.Comment;
import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import com.zzangmin.gesipan.layer.basiccrud.entity.PostCategory;
import com.zzangmin.gesipan.layer.basiccrud.repository.CommentRepository;
import com.zzangmin.gesipan.layer.basiccrud.repository.PostCategoryRepository;
import com.zzangmin.gesipan.layer.basiccrud.repository.PostRepository;
import com.zzangmin.gesipan.layer.embeddable.BaseTime;
import com.zzangmin.gesipan.layer.login.entity.UserRole;
import com.zzangmin.gesipan.layer.login.entity.Users;
import com.zzangmin.gesipan.layer.login.repository.UsersRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService commentService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostCategoryRepository postCategoryRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    CommentRepository commentRepository;

    PostCategory postCategory;
    Users user;
    Post post;

    @BeforeEach
    void setUp() {
        postCategory = PostCategory.builder()
                .categoryName(Categories.FREE)
                .build();
        user = Users.builder()
                .userEmail("ckdals1234@naver.com")
                .userName("이창민")
                .userNickname("zzangmin")
                .userRole(UserRole.NORMAL)
                .baseTime(new BaseTime(LocalDateTime.of(2022,2,2,2,2), LocalDateTime.of(2022,2,2,2,2)))
                .build();

        post = Post.builder()
                .postSubject("test제목")
                .postContent("test내용")
                .user(user)
                .postCategory(postCategory)
                .baseTime(new BaseTime(LocalDateTime.of(2022,2,2,2,2), LocalDateTime.of(2022,2,2,2,2)))
                .hitCount(0L)
                .build();
        usersRepository.save(user).getUserId();
        postCategoryRepository.save(postCategory);
        postRepository.save(post).getPostId();
    }

    @Test
    void pagination() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Comment comment1 = Comment.builder()
                .commentContent("testContent1")
                .baseTime(new BaseTime(now, now))
                .user(user)
                .post(post)
                .build();
        now = LocalDateTime.now();
        Comment comment2 = Comment.builder()
                .commentContent("testContent2")
                .baseTime(new BaseTime(now, now))
                .user(user)
                .post(post)
                .build();
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        //when
        List<CommentResponse> pagination = commentService.pagination(post.getPostId(), PageRequest.of(0, 10));
        //then
        Assertions.assertThat(pagination.get(0).getCommentId()).isEqualTo(comment1.getCommentId());
        Assertions.assertThat(pagination.get(1).getCommentId()).isEqualTo(comment2.getCommentId());
        Assertions.assertThat(pagination.get(0).getContent()).isEqualTo(comment1.getCommentContent());
        Assertions.assertThat(pagination.get(1).getContent()).isEqualTo(comment2.getCommentContent());
        Assertions.assertThat(pagination.get(0).getBaseTime()).isEqualTo(comment1.getBaseTime());
        Assertions.assertThat(pagination.get(1).getBaseTime()).isEqualTo(comment2.getBaseTime());
        Assertions.assertThat(pagination.get(0).getUserId()).isEqualTo(comment1.getUser().getUserId());
        Assertions.assertThat(pagination.get(1).getUserId()).isEqualTo(comment2.getUser().getUserId());
    }
}