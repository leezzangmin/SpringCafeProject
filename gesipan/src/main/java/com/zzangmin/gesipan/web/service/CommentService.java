package com.zzangmin.gesipan.web.service;

import com.zzangmin.gesipan.dao.CommentRepository;
import com.zzangmin.gesipan.dao.PostRepository;
import com.zzangmin.gesipan.dao.UsersRepository;
import com.zzangmin.gesipan.web.dto.comment.CommentSaveRequest;
import com.zzangmin.gesipan.web.dto.comment.CommentUpdateRequest;
import com.zzangmin.gesipan.web.dto.comment.PersonalCommentsResponse;
import com.zzangmin.gesipan.web.entity.Comment;
import com.zzangmin.gesipan.web.entity.Post;
import com.zzangmin.gesipan.web.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UsersRepository usersRepository;
    private final PostRepository postRepository;

    public Long save(CommentSaveRequest commentSaveRequest, Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다. 잘못된 입력"));
        Post post = postRepository.findById(commentSaveRequest.getReferencePostId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시물이 없습니다. 잘못된 입력"));
        Comment comment = commentSaveRequest.toEntity(user, post);
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
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    @Transactional(readOnly = true)
    public PersonalCommentsResponse userComments(Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다. 잘못된 입력"));
        List<Comment> comments = commentRepository.findAllByUserIdWithPostId(userId);
        return PersonalCommentsResponse.of(user, comments);
    }

    private void validateTime(LocalDateTime updatedAt, LocalDateTime createdAt) {
        if (updatedAt.isBefore(createdAt) || updatedAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("요청된 시간이 조건에 부합하지 않습니다.");
        }
    }

    private void validateCommentOwner(Long userId, Comment comment) {
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 유저의 댓글이 아닙니다.");
        }
    }

}
