package com.zzangmin.gesipan.web.service;

import com.zzangmin.gesipan.dao.CommentRepository;
import com.zzangmin.gesipan.dao.PostRepository;
import com.zzangmin.gesipan.dao.UserRepository;
import com.zzangmin.gesipan.web.dto.comment.CommentSaveRequest;
import com.zzangmin.gesipan.web.dto.comment.CommentUpdateRequest;
import com.zzangmin.gesipan.web.entity.Comment;
import com.zzangmin.gesipan.web.entity.Post;
import com.zzangmin.gesipan.web.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Long save(CommentSaveRequest commentSaveRequest) {
        Users user = userRepository.findById(commentSaveRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다. 잘못된 입력"));
        Post post = postRepository.findById(commentSaveRequest.getReferencePostId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시물이 없습니다. 잘못된 입력"));
        Comment comment = commentSaveRequest.toEntity(user, post);
        return commentRepository.save(comment).getCommentId();
    }

    public void delete(Long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글이 없습니다. 잘못된 입력"));
        commentRepository.deleteById(commentId);
    }

    public void update(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글이 없습니다. 잘못된 입력"));
        if (commentUpdateRequest.getUpdatedAt().isBefore(comment.getCreatedAt()) || commentUpdateRequest.getUpdatedAt().isBefore(comment.getUpdatedAt())) {
            throw new IllegalArgumentException("요청된 시간이 조건에 부합하지 않습니다.");
        }
        comment.update(commentUpdateRequest.getCommentContent(), commentUpdateRequest.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }
}
