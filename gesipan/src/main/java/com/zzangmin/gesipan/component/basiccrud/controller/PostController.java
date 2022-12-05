package com.zzangmin.gesipan.component.basiccrud.controller;

import com.zzangmin.gesipan.component.login.argumentresolver.Auth;
import com.zzangmin.gesipan.component.login.argumentresolver.OptionalAuth;
import com.zzangmin.gesipan.component.basiccrud.dto.post.*;
import com.zzangmin.gesipan.component.basiccrud.service.PostService;
import com.zzangmin.gesipan.component.caching.redis.RedisPostHitCountBulkUpdateService;
import com.zzangmin.gesipan.component.basiccrud.service.TemporaryPostService;
import com.zzangmin.gesipan.component.basiccrud.dto.temporarypost.TemporaryPostLoadResponse;
import com.zzangmin.gesipan.component.basiccrud.dto.temporarypost.TemporaryPostSaveRequest;
import com.zzangmin.gesipan.component.basiccrud.entity.Categories;

import java.time.Duration;
import java.time.Period;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final int VALIDATE_DIFFERENCE_OF_TWO_LOCAL_DATETIME_SECONDS = 60;
    private final int MAX_SEARCH_BETWEEN_DAY_AMOUNT = 14;

    private final PostService postService;
    private final RedisPostHitCountBulkUpdateService redisPostHitCountBulkUpdateService;
    private final TemporaryPostService temporaryPostService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponse> singlePost(@PathVariable Long postId, HttpServletRequest httpServletRequest, @OptionalAuth Optional<Long> userId) {
        log.info("postId: {}", postId);
        PostResponse singlePost = postService.findOne(postId, userId);
        String clientAddress = httpServletRequest.getRemoteAddr();
        redisPostHitCountBulkUpdateService.increasePostHitCount(clientAddress, postId);
        return ResponseEntity.ok(singlePost);
    }

    @PostMapping("/post")
    public ResponseEntity<Long> createPost(@RequestBody @Valid PostSaveRequest postSaveRequest, @Auth Long userId) {
        log.info("post create: {}", postSaveRequest);
        validateRequestDate(postSaveRequest.getCreatedAt());
        long savedPostId = postService.save(userId, postSaveRequest);
        return ResponseEntity.ok(savedPostId);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> removePost(@PathVariable Long postId, @Auth Long userId) {
        postService.delete(postId, userId);
        return ResponseEntity.ok("post remove success");
    }

    @PatchMapping("/post/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody @Valid PostUpdateRequest postUpdateRequest, @Auth Long userId) {
        log.info("post update :{}", postUpdateRequest);
        validateRequestDate(postUpdateRequest.getUpdatedAt());
        postService.update(postId, postUpdateRequest, userId);
        return ResponseEntity.ok("post update success");
    }

    @GetMapping("/posts")
    public ResponseEntity<PostsPageResponse> postPagination(@RequestParam String categoryName, Pageable pageable) {
        log.info("post pagination: {}", pageable);
        Long categoryId = Categories.castCategoryNameToCategoryId(categoryName);
        PostsPageResponse postsPageResponse = postService.pagination(categoryId, pageable);
        return ResponseEntity.ok(postsPageResponse);
    }

    @PostMapping("/post/recommend")
    public ResponseEntity<String> recommendPost(@RequestBody @Valid PostRecommendRequest postRecommendRequest) {
        log.info("post recommend : {}", postRecommendRequest);
        postService.postRecommend(postRecommendRequest);
        return ResponseEntity.ok("recommend success");
    }

    @GetMapping("/posts/my")
    public ResponseEntity<PersonalPostsResponse> myPosts(@Auth Long userId, Pageable pageable) {
        log.info("myPost requested userId : {} ", userId);
        PersonalPostsResponse personalPostsResponse = postService.userPosts(userId, pageable);
        return ResponseEntity.ok(personalPostsResponse);
    }

    @GetMapping("/posts/recommend")
    public ResponseEntity<PostRecommendsResponse> recommendedPosts(@Auth Long userId, Pageable pageable) {
        log.info("recommendedPosts requested userId : {} ", userId);
        PostRecommendsResponse recommendedPost = postService.findRecommendedPost(userId, pageable);
        return ResponseEntity.ok(recommendedPost);
    }

    @PostMapping("/post/temporary")
    public void temporarySave(@RequestBody @Valid TemporaryPostSaveRequest temporaryPostSaveRequest, @Auth Long userId) {
        temporaryPostService.postTemporarySave(userId, temporaryPostSaveRequest);
    }

    @GetMapping("/post/temporary")
    public ResponseEntity<TemporaryPostLoadResponse> temporaryLoad(@Auth Long userId) {
        TemporaryPostLoadResponse temporaryPostLoadResponse = temporaryPostService.temporaryPostLoad(userId);
        return ResponseEntity.ok(temporaryPostLoadResponse);
    }

    @GetMapping("/post/search")
    public ResponseEntity<PostSearchResponse> searchPost(@Valid PostSearchRequest postSearchRequest) {
        validateSearchParameters(postSearchRequest);
        log.info("postSearchRequest: {}", postSearchRequest);
        PostSearchResponse postSearchResponse = postService.searchPosts(postSearchRequest);
        return ResponseEntity.ok(postSearchResponse);
    }

    private void validateSearchParameters(PostSearchRequest postSearchRequest) {
        if (postSearchRequest.getUserNickname() == null && postSearchRequest.getStartAt() == null && postSearchRequest.getEndAt() == null) {
            throw new IllegalArgumentException("모든 조건이 null입니다.");
        }
        validateDateRange(postSearchRequest.getStartAt(), postSearchRequest.getEndAt());
    }

    private void validateDateRange(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt == null && endAt == null) {
            return;
        }
        if ((startAt == null && endAt != null) || (startAt != null && endAt == null)) {
            throw new IllegalArgumentException("입력한 시간이 조건에 맞지 않습니다.");
        }
        if (startAt.isAfter(endAt)) {
            throw new IllegalArgumentException("입력한 시간이 조건에 맞지 않습니다.");
        }
        if (ChronoUnit.DAYS.between(endAt, startAt) > MAX_SEARCH_BETWEEN_DAY_AMOUNT) {
            throw new IllegalArgumentException("너무 긴 범위의 검색기간입니다. " + MAX_SEARCH_BETWEEN_DAY_AMOUNT +"일 이하로 지정해주세요.");
        }
    }

    private void validateRequestDate(LocalDateTime givenDate) {
        if (ChronoUnit.SECONDS.between(LocalDateTime.now(), givenDate) > VALIDATE_DIFFERENCE_OF_TWO_LOCAL_DATETIME_SECONDS) {
            throw new IllegalArgumentException("입력된 날짜가 조건에 부합하지 않습니다.");
        }
    }

}
