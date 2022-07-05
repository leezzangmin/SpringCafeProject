package com.zzangmin.gesipan.web.controller;

import com.zzangmin.gesipan.web.dto.PostResponse;
import com.zzangmin.gesipan.web.dto.PostSaveRequest;
import com.zzangmin.gesipan.web.dto.PostUpdateRequest;
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
        return postService.findOne(postId);
    }

    @PostMapping("/post")
    public ResponseEntity createPost(@Validated @RequestBody PostSaveRequest postSaveRequest, BindingResult bindingResult) {
        return ResponseEntity.ok(postService.save(postSaveRequest));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity removePost(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.ok("post remove success");
    }

    @PatchMapping("/post/{postId}")
    public ResponseEntity updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        postService.update(postId, postUpdateRequest);
        return ResponseEntity.ok("post update success");
    }


}
