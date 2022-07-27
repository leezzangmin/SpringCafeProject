package com.zzangmin.gesipan.web.controller;

import com.zzangmin.gesipan.web.dto.post.*;
import com.zzangmin.gesipan.web.entity.Categories;
import com.zzangmin.gesipan.web.service.PostService;
import com.zzangmin.gesipan.web.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final RedisService redisService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponse> singlePost(@PathVariable Long postId, HttpServletRequest httpServletRequest) {
        String clientAddress = httpServletRequest.getRemoteAddr();
        return ResponseEntity.ok(postService.findOne(postId, clientAddress));
    }

    @PostMapping("/post")
    public ResponseEntity<Long> createPost(@RequestBody @Valid PostSaveRequest postSaveRequest) {
        return ResponseEntity.ok(postService.save(postSaveRequest));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> removePost(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.ok("post remove success");
    }

    @PatchMapping("/post/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody @Valid PostUpdateRequest postUpdateRequest) {
        postService.update(postId, postUpdateRequest);
        return ResponseEntity.ok("post update success");
    }

    @GetMapping("/posts")
    public ResponseEntity<PostsPageResponse> postPagination(@RequestParam String categoryName, Pageable pageable) {
        Long categoryId = Categories.castCategoryNameToCategoryId(categoryName);
        PostsPageResponse postsPageResponse = postService.pagination(categoryId, pageable);
        return ResponseEntity.ok(postsPageResponse);
    }

    @PostMapping("/post/recommend")
    public ResponseEntity<String> recommendPost(@RequestBody @Valid PostRecommendRequest postRecommendRequest) {
        postService.postRecommendCount(postRecommendRequest);
        return ResponseEntity.ok("recommend success");
    }

}
