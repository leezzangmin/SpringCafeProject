package com.zzangmin.gesipan.layer.basiccrud.service;

import com.zzangmin.gesipan.layer.basiccrud.dto.comment.CommentResponse;
import com.zzangmin.gesipan.layer.basiccrud.dto.comment.CommentUpdateRequest;
import com.zzangmin.gesipan.layer.basiccrud.entity.Comment;
import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import com.zzangmin.gesipan.layer.basiccrud.repository.CommentRepository;
import com.zzangmin.gesipan.layer.basiccrud.repository.PostCategoryRepository;
import com.zzangmin.gesipan.layer.basiccrud.repository.PostRepository;
import com.zzangmin.gesipan.layer.login.entity.Users;
import com.zzangmin.gesipan.layer.login.repository.UsersRepository;
import com.zzangmin.gesipan.testfactory.EntityFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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


    @DisplayName("댓글 페이징 조회 서비스로직을 수행하면 정확한 댓글 내용이 반환되어야 한다.")
    @Test
    void pagination() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        Comment comment1 = EntityFactory.generateCommentObject(post, user);
        Comment comment2 = EntityFactory.generateCommentObject(post, user);

        usersRepository.save(user);
        postCategoryRepository.save(post.getPostCategory());
        postRepository.save(post);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        //when
        List<CommentResponse> pagination = commentService.pagination(comment1.getPost().getPostId(), PageRequest.of(0, 10));
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

    @DisplayName("DB에 존재하지 않는 commentId로 삭제 요청을 보내면 오류가 발생해야 한다.")
    @Test
    void delete_NoCommentIdError() {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(() -> commentService.delete(9999999L, 12345L));
    }

    @DisplayName("댓글 소유자가 아닌 Users가 요청하면 오류가 발생해야 한다.")
    @Test
    void delete_NotOwnerError() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        Comment comment = EntityFactory.generateCommentObject(post, user);
        usersRepository.save(user);
        postCategoryRepository.save(post.getPostCategory());
        postRepository.save(post);
        commentRepository.save(comment);
        //when
        //then
        Assertions.assertThatThrownBy(() -> commentService.delete(comment.getCommentId(), 1234567895413L));
    }

    @DisplayName("댓글 소유자인 Users가 요청하면 삭제되어야 한다.")
    @Test
    void delete_Owner() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        Comment comment = EntityFactory.generateCommentObject(post, user);
        usersRepository.save(user);
        postCategoryRepository.save(post.getPostCategory());
        postRepository.save(post);
        commentRepository.save(comment);
        //when
        commentService.delete(comment.getCommentId(), user.getUserId());
        //then
        Assertions.assertThat(commentRepository.findById(comment.getCommentId())).isEmpty();
    }

    @DisplayName("DB에 존재하지 않는 commentId로 갱신 요청을 보내면 오류가 발생해야 한다.")
    @Test
    void update_NoCommentId() {
        //given
        Long noCommentId = 1234567789L;
        Long noUserId = 123123123123123L;
        CommentUpdateRequest commentUpdateRequest = new CommentUpdateRequest("fake", LocalDateTime.now());
        //when
        //then
        Assertions.assertThatThrownBy(() -> commentService.update(noCommentId, commentUpdateRequest, noUserId));
    }

}
