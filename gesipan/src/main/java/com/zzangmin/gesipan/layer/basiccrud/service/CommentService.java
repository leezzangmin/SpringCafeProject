package com.zzangmin.gesipan.layer.basiccrud.service;

import com.zzangmin.gesipan.layer.basiccrud.dto.comment.CommentResponse;
import com.zzangmin.gesipan.layer.basiccrud.repository.CommentRepository;
import com.zzangmin.gesipan.layer.basiccrud.repository.PostRepository;
import com.zzangmin.gesipan.layer.login.repository.UsersRepository;
import com.zzangmin.gesipan.layer.basiccrud.dto.comment.CommentSaveRequest;
import com.zzangmin.gesipan.layer.basiccrud.dto.comment.CommentUpdateRequest;
import com.zzangmin.gesipan.layer.basiccrud.dto.comment.PersonalCommentsResponse;
import com.zzangmin.gesipan.layer.notification.dto.notification.NotificationCreateRequest;
import com.zzangmin.gesipan.layer.basiccrud.entity.Comment;
import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import com.zzangmin.gesipan.layer.login.entity.Users;
import com.zzangmin.gesipan.layer.notification.entity.NotificationType;
import com.zzangmin.gesipan.layer.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UsersRepository usersRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    public Long save(CommentSaveRequest commentSaveRequest, Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다. 잘못된 입력"));
        Post post = postRepository.findByIdWithUser(commentSaveRequest.getReferencePostId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시물이 없습니다. 잘못된 입력"));
        Comment comment = commentSaveRequest.toEntity(user, post);

        createCommentNotification(user, post);

        return commentRepository.save(comment).getCommentId();
    }

    public void delete(Long commentId, Long userId) {
        Comment comment = commentRepository.findByIdWithUsers(commentId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글이 없습니다. 잘못된 입력"));
        validateCommentOwner(userId, comment);
        commentRepository.delete(comment);
    }

    public void update(Long commentId, CommentUpdateRequest commentUpdateRequest, Long userId) {
        Comment comment = commentRepository.findByIdWithUsers(commentId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글이 없습니다. 잘못된 입력"));
        validateTime(commentUpdateRequest.getUpdatedAt(), comment.getCreatedAt());
        validateCommentOwner(userId, comment);
        comment.update(commentUpdateRequest.getCommentContent(), commentUpdateRequest.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    public PersonalCommentsResponse userComments(Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다. 잘못된 입력"));
        List<Comment> comments = commentRepository.findAllByUserIdWithPostId(userId);
        return PersonalCommentsResponse.of(user, comments);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> pagination(Long postId, Pageable pageable) {
        List<Long> commentIds = commentRepository.findCommentIdsByPaginationByPostId(postId, pageable);
        final List<Comment> comments = commentRepository.findByIdsWithUsers(commentIds);
        return comments.stream()
                .map(c -> CommentResponse.of(c))
                .collect(Collectors.toList());
    }

    private void validateTime(LocalDateTime updatedAt, LocalDateTime createdAt) {
        if ((updatedAt.isBefore(createdAt) || updatedAt.isBefore(createdAt))) {
            throw new IllegalArgumentException("요청된 시간이 조건에 부합하지 않습니다.");
        }
    }

    private void createCommentNotification(Users user, Post post) {
        if (user.getUserId().equals(post.getUser().getUserId())) {
            return;
        }

        NotificationCreateRequest request = NotificationCreateRequest.builder()
                .targetUser(post.getUser())
                .publishedUser(user)
                .notificationType(NotificationType.COMMENT)
                .referencePost(post)
                .notificationMessage(null)
                .build();
        notificationService.createNotification(request);
    }

    private void validateCommentOwner(Long userId, Comment comment) {
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 유저의 댓글이 아닙니다.");
        }
    }


}
