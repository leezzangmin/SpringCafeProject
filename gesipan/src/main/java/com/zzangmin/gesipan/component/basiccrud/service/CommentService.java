package com.zzangmin.gesipan.component.basiccrud.service;

import com.zzangmin.gesipan.component.basiccrud.dto.comment.CommentResponse;
import com.zzangmin.gesipan.component.basiccrud.repository.CommentRepository;
import com.zzangmin.gesipan.component.basiccrud.repository.PostRepository;
import com.zzangmin.gesipan.component.basiccrud.dto.comment.CommentSaveRequest;
import com.zzangmin.gesipan.component.basiccrud.dto.comment.CommentUpdateRequest;
import com.zzangmin.gesipan.component.basiccrud.dto.comment.PersonalCommentsResponse;
import com.zzangmin.gesipan.component.basiccrud.entity.Comment;
import com.zzangmin.gesipan.component.basiccrud.entity.Post;
import com.zzangmin.gesipan.component.login.entity.Users;
import com.zzangmin.gesipan.component.login.service.UsersService;
import com.zzangmin.gesipan.component.notification.entity.NotificationType;
import com.zzangmin.gesipan.component.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final UsersService usersService;
    private final PostRepository postRepository;

    public Long save(CommentSaveRequest commentSaveRequest, Long userId) {
        Users user = usersService.findOne(userId);
        Post post = postRepository.findByIdWithUser(commentSaveRequest.getReferencePostId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시물이 없습니다. 잘못된 입력"));
        Comment comment = commentSaveRequest.toEntity(user, post);

        notificationService.createNotification(user, post, NotificationType.COMMENT, LocalDateTime.now());

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
        Users user = usersService.findOne(userId);
        List<Comment> comments = commentRepository.findAllByUserIdWithPostId(userId);
        return PersonalCommentsResponse.of(user, comments);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> pagination(Long postId, Pageable pageable) {
        List<Long> commentIds = commentRepository.findCommentIdsByPaginationByPostId(postId, pageable);
        return commentRepository.findByIdsWithUsers(commentIds);
    }

    private void validateTime(LocalDateTime updatedAt, LocalDateTime createdAt) {
        if ((updatedAt.isBefore(createdAt) || updatedAt.isEqual(createdAt))) {
            throw new IllegalArgumentException("요청된 시간이 조건에 부합하지 않습니다.");
        }
    }

    private void validateCommentOwner(Long userId, Comment comment) {
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 유저의 댓글이 아닙니다.");
        }
    }

}
