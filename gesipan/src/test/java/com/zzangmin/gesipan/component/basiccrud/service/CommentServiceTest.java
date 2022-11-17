package com.zzangmin.gesipan.component.basiccrud.service;

import com.zzangmin.gesipan.component.basiccrud.dto.comment.CommentResponse;
import com.zzangmin.gesipan.component.basiccrud.dto.comment.CommentSaveRequest;
import com.zzangmin.gesipan.component.basiccrud.dto.comment.CommentUpdateRequest;
import com.zzangmin.gesipan.component.basiccrud.dto.comment.PersonalCommentsResponse;
import com.zzangmin.gesipan.component.basiccrud.entity.Comment;
import com.zzangmin.gesipan.component.basiccrud.entity.Post;
import com.zzangmin.gesipan.component.basiccrud.repository.CommentRepository;
import com.zzangmin.gesipan.component.basiccrud.repository.PostCategoryRepository;
import com.zzangmin.gesipan.component.basiccrud.repository.PostRepository;
import com.zzangmin.gesipan.component.login.entity.Users;
import com.zzangmin.gesipan.component.login.repository.UsersRepository;
import com.zzangmin.gesipan.testfactory.EntityFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

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
        Assertions.assertThat(pagination.get(0).getCommentContent()).isEqualTo(comment1.getCommentContent());
        Assertions.assertThat(pagination.get(1).getCommentContent()).isEqualTo(comment2.getCommentContent());
        Assertions.assertThat(pagination.get(0).getCreatedAt()).isEqualTo(comment1.getBaseTime().getCreatedAt());
        Assertions.assertThat(pagination.get(1).getCreatedAt()).isEqualTo(comment2.getBaseTime().getCreatedAt());
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

    @DisplayName("댓글 소유자인 Users가 삭제를 요청하면 삭제되어야 한다.")
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

    @DisplayName("갱신 요청 시간이 comment 생성시간과 같거나 앞서면 오류가 발생해야 한다.")
    @Test
    void update_invalidDateTime() {
        //given
        CommentUpdateRequest commentUpdateRequest = new CommentUpdateRequest("fake", LocalDateTime.of(1955,05,05,05,05,05));
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        Comment comment = EntityFactory.generateCommentObject(post, user);
        postCategoryRepository.save(post.getPostCategory());
        usersRepository.save(user);
        postRepository.save(post);
        commentRepository.save(comment);
        //when
        //then
        Assertions.assertThatThrownBy(() -> commentService.update(comment.getCommentId(), commentUpdateRequest, user.getUserId()));
    }

    @DisplayName("댓글 소유자가 아닌 Users가 갱신을 요청하면 오류가 발생해야 한다.")
    @Test
    void update_notOwner() {
        //given
        CommentUpdateRequest commentUpdateRequest = new CommentUpdateRequest("fake", LocalDateTime.now());
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        Comment comment = EntityFactory.generateCommentObject(post, user);
        usersRepository.save(user);
        postCategoryRepository.save(post.getPostCategory());
        postRepository.save(post);
        commentRepository.save(comment);
        Long fakeUserId = 1234125135614256134L;
        //when
        //then
        Assertions.assertThatThrownBy(() -> commentService.update(comment.getCommentId(), commentUpdateRequest, fakeUserId));
    }

    @DisplayName("댓글 소유자가 정상적인 요청을 보내면 갱신이 되어야 한다.")
    @Test
    void update_owner() {
        //given
        LocalDateTime now = LocalDateTime.now();
        CommentUpdateRequest commentUpdateRequest = new CommentUpdateRequest("update_request_string", now);
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        Comment comment = EntityFactory.generateCommentObject(post, user);
        usersRepository.save(user);
        postCategoryRepository.save(post.getPostCategory());
        postRepository.save(post);
        commentRepository.save(comment);
        //when
        commentService.update(comment.getCommentId(), commentUpdateRequest, user.getUserId());
        //then
        Comment updatedComment = commentRepository.findById(comment.getCommentId()).get();
        Assertions.assertThat(updatedComment.getCommentContent()).isEqualTo("update_request_string");
        Assertions.assertThat(updatedComment.getUpdatedAt()).isEqualTo(now);
    }

    @DisplayName("존재하지 않는 userId로 유저의 댓글조회 요청을 보내면 오류가 발생해야 한다.")
    @Test
    void userComments_invalidUserId() {
        //given
        Long invalidUserId = 123145345L;
        //when
        //then
        Assertions.assertThatThrownBy(() -> commentService.userComments(invalidUserId));
    }

    @DisplayName("정상 userId로 요청을 보내면 해당 유저가 작성한 댓글이 반환되어야 한다.")
    @Test
    void userComments_validUserId() {
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
        PersonalCommentsResponse userComments = commentService.userComments(user.getUserId());
        //then
        Assertions.assertThat(userComments.getUserId()).isEqualTo(user.getUserId());

        Assertions.assertThat(userComments.getSingleCommentResponses().get(0).getCommentId()).isEqualTo(comment1.getCommentId());
        Assertions.assertThat(userComments.getSingleCommentResponses().get(1).getCommentId()).isEqualTo(comment2.getCommentId());

        Assertions.assertThat(userComments.getSingleCommentResponses().get(0).getReferencePostId()).isEqualTo(comment1.getPost().getPostId());
        Assertions.assertThat(userComments.getSingleCommentResponses().get(1).getReferencePostId()).isEqualTo(comment2.getPost().getPostId());
    }

    @DisplayName("유효하지 않은 userId로 저장요청을 보내면 오류가 발생해야 한다.")
    @Test
    void save_invalidUserId() {
        //given
        CommentSaveRequest commentSaveRequest = new CommentSaveRequest(123L, "comment_content", LocalDateTime.now());
        Long invalidUserId = 12347189461L;
        //when
        //then
        Assertions.assertThatThrownBy(() -> commentService.save(commentSaveRequest, invalidUserId));
    }

    @DisplayName("유효하지 않은 postId로 저장요청을 보내면 오류가 발생해야 한다.")
    @Test
    void save_invalidPostId() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Long invalidPostId = 99923549259L;
        CommentSaveRequest commentSaveRequest = new CommentSaveRequest(invalidPostId, "comment_content", LocalDateTime.now());
        usersRepository.save(user);
        //when
        //then
        Assertions.assertThatThrownBy(() -> commentService.save(commentSaveRequest, user.getUserId()));
    }

    @DisplayName("저장이 정상적으로 수행되어야 한다.")
    @Test
    void save() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        postCategoryRepository.save(post.getPostCategory());
        usersRepository.save(user);
        postRepository.save(post);
        CommentSaveRequest commentSaveRequest = new CommentSaveRequest(post.getPostId(), "save_comment_content", LocalDateTime.of(2022,02,02,02,02,02));
        //when
        Long savedId = commentService.save(commentSaveRequest, user.getUserId());
        //then
        Comment comment = commentRepository.findById(savedId).get();
        Assertions.assertThat(comment.getCommentId()).isEqualTo(savedId);
        Assertions.assertThat(comment.getCommentContent()).isEqualTo("save_comment_content");
        Assertions.assertThat(comment.getPost().getPostId()).isEqualTo(post.getPostId());
        Assertions.assertThat(comment.getUser().getUserId()).isEqualTo(user.getUserId());
    }


}
