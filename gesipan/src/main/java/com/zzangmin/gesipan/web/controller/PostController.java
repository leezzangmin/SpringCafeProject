package com.zzangmin.gesipan.web.controller;

import com.zzangmin.gesipan.web.dto.PostResponse;
import com.zzangmin.gesipan.web.dto.PostSaveRequest;
import com.zzangmin.gesipan.web.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping("/post/{postId}")
    public PostResponse singlePost(@PathVariable Long postId) {
        postService.findOne(postId);
        return new PostResponse();
    }

    @PostMapping("/post")
    public ResponseEntity createPost(@Validated @RequestBody PostSaveRequest postSaveRequest, BindingResult bindingResult) {
        return ResponseEntity.ok(postService.save(postSaveRequest));
    }

}
