package com.zzangmin.gesipan.web.controller;

import com.zzangmin.gesipan.web.dto.CommentSaveRequest;
import com.zzangmin.gesipan.web.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity createComment(@RequestBody @Valid CommentSaveRequest commentSaveRequest) {
        Long savedCommentId = commentService.save(commentSaveRequest);
        return ResponseEntity.ok(savedCommentId);
    }

}
