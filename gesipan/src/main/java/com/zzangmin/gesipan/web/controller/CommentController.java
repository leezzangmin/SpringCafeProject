package com.zzangmin.gesipan.web.controller;

import com.zzangmin.gesipan.web.dto.comment.CommentSaveRequest;
import com.zzangmin.gesipan.web.dto.comment.CommentUpdateRequest;
import com.zzangmin.gesipan.web.dto.comment.PersonalCommentsResponse;
import com.zzangmin.gesipan.web.jwt.JwtProvider;
import com.zzangmin.gesipan.web.service.CommentService;
import com.zzangmin.gesipan.web.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final JwtProvider jwtProvider;

    @PostMapping("/comment")
    public ResponseEntity<Long> createComment(@RequestBody @Valid CommentSaveRequest commentSaveRequest) {
        log.info("comment save: {}", commentSaveRequest);
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

    @GetMapping("/comments/my")
    public ResponseEntity<PersonalCommentsResponse> myComments(HttpServletRequest request) {
        String jwt = jwtProvider.resolveToken(request);
        log.info("my comments jwt: {}", jwt);
        Long userId = jwtProvider.getUserId(jwt);

        return ResponseEntity.ok(commentService.userComments(userId));
    }

}
