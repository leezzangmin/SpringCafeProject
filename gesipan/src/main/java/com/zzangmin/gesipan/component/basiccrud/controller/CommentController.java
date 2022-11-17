package com.zzangmin.gesipan.component.basiccrud.controller;

import com.zzangmin.gesipan.component.basiccrud.dto.comment.CommentResponse;
import com.zzangmin.gesipan.component.login.argumentresolver.Auth;
import com.zzangmin.gesipan.component.basiccrud.dto.comment.CommentSaveRequest;
import com.zzangmin.gesipan.component.basiccrud.dto.comment.CommentUpdateRequest;
import com.zzangmin.gesipan.component.basiccrud.dto.comment.PersonalCommentsResponse;
import com.zzangmin.gesipan.component.basiccrud.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<Long> createComment(@RequestBody @Valid CommentSaveRequest commentSaveRequest, @Auth Long userId) {
        log.info("comment save: {}", commentSaveRequest);
        Long savedCommentId = commentService.save(commentSaveRequest, userId);
        return ResponseEntity.ok(savedCommentId);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Long> deleteComment(@PathVariable Long commentId, @Auth Long userId) {
        log.info("comment delete: {}", commentId);
        commentService.delete(commentId, userId);
        return ResponseEntity.ok(commentId);
    }

    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<Long> updateComment(@PathVariable Long commentId, @RequestBody @Valid CommentUpdateRequest commentUpdateRequest, @Auth Long userId) {
        commentService.update(commentId, commentUpdateRequest, userId);
        return ResponseEntity.ok(commentId);
    }

    @GetMapping("/comments/my")
    public ResponseEntity<PersonalCommentsResponse> myComments(@Auth Long userId) {
        log.info("my comments userId: {}", userId);
        return ResponseEntity.ok(commentService.userComments(userId));
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponse>> commentPagination(@RequestParam Long postId, Pageable pageable) {
        log.info("comment pagination: {}", pageable);
        List<CommentResponse> commentResponses = commentService.pagination(postId, pageable);
        return ResponseEntity.ok(commentResponses);
    }

}