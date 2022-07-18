package com.zzangmin.gesipan.web.controller;

import com.zzangmin.gesipan.web.dto.PostResponse;
import com.zzangmin.gesipan.web.dto.PostSaveRequest;
import com.zzangmin.gesipan.web.dto.PostUpdateRequest;
import com.zzangmin.gesipan.web.dto.PostsPageResponse;
import com.zzangmin.gesipan.web.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponse> singlePost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.findOne(postId));
    }

    @PostMapping("/post")
    public ResponseEntity<Long> createPost(@Validated @RequestBody PostSaveRequest postSaveRequest, BindingResult bindingResult) {
        return ResponseEntity.ok(postService.save(postSaveRequest));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> removePost(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.ok("post remove success");
    }

    @PatchMapping("/post/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        postService.update(postId, postUpdateRequest);
        return ResponseEntity.ok("post update success");
    }

    @GetMapping("/posts")
    public ResponseEntity<PostsPageResponse> postPagination(@RequestParam Long categoryId, Pageable pageable) {
        PostsPageResponse postsPageResponse = postService.pagination(categoryId, pageable);
        return ResponseEntity.ok(postsPageResponse);
    }


}
