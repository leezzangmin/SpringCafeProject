package com.zzangmin.gesipan.web.controller;

import com.zzangmin.gesipan.web.dto.CommentSaveRequest;
import com.zzangmin.gesipan.web.dto.CommentUpdateRequest;
import com.zzangmin.gesipan.web.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<Long> createComment(@RequestBody @Valid CommentSaveRequest commentSaveRequest) {
        Long savedCommentId = commentService.save(commentSaveRequest);
        return ResponseEntity.ok(savedCommentId);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok("delete success!");
    }

    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId, @RequestBody @Valid CommentUpdateRequest commentUpdateRequest) {
        commentService.update(commentId, commentUpdateRequest);
        return ResponseEntity.ok("update success!");
    }

}
