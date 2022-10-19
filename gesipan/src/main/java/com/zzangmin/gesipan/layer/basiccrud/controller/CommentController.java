package com.zzangmin.gesipan.layer.basiccrud.controller;

import com.zzangmin.gesipan.argumentresolver.Auth;
import com.zzangmin.gesipan.layer.basiccrud.dto.comment.CommentSaveRequest;
import com.zzangmin.gesipan.layer.basiccrud.dto.comment.CommentUpdateRequest;
import com.zzangmin.gesipan.layer.basiccrud.dto.comment.PersonalCommentsResponse;
import com.zzangmin.gesipan.layer.basiccrud.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

}
