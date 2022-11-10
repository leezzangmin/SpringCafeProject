package com.zzangmin.gesipan.layer.basiccrud.controller;

import com.zzangmin.gesipan.layer.login.argumentresolver.Auth;
import com.zzangmin.gesipan.layer.login.service.JwtProvider;
import com.zzangmin.gesipan.layer.basiccrud.dto.post.*;
import com.zzangmin.gesipan.layer.basiccrud.service.PostService;
import com.zzangmin.gesipan.layer.caching.redis.RedisPostHitCountBulkUpdateService;
import com.zzangmin.gesipan.layer.basiccrud.service.TemporaryPostService;
import com.zzangmin.gesipan.layer.basiccrud.dto.temporarypost.TemporaryPostLoadResponse;
import com.zzangmin.gesipan.layer.basiccrud.dto.temporarypost.TemporaryPostSaveRequest;
import com.zzangmin.gesipan.layer.basiccrud.entity.Categories;
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

    private final int validateDifferenceOfTwoLocalDateTimeSeconds = 60;

    private final PostService postService;
    private final RedisPostHitCountBulkUpdateService redisPostHitCountBulkUpdateService;
    private final TemporaryPostService temporaryPostService;
    private final JwtProvider jwtProvider;


    // TODO: 서블릿에서 아이피 뽑아서 레디스에 캐싱하는거 말고 JWT로 구분해서 넣는것은 어떤지
    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponse> singlePost(@PathVariable Long postId, HttpServletRequest httpServletRequest) {
        log.info("postId: {}", postId);
        String clientAddress = httpServletRequest.getRemoteAddr();
        Optional<Long> userId = jwtProvider.getUserId(httpServletRequest);
        PostResponse singlePost = postService.findOne(postId, userId);
        redisPostHitCountBulkUpdateService.increasePostHitCount(clientAddress, postId);
        return ResponseEntity.ok(singlePost);
    }

    // TODO: jwt에서 정보 뽑아오기. DTO 수정해야함
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

    // TODO: @RequestParam으로 받는 userId 추후 개선 -> ArgumentResolver 쓰기;;
    @GetMapping("/posts/my")
    public ResponseEntity<PersonalPostsResponse> myPosts(@Auth Long userId) {
        PersonalPostsResponse personalPostsResponse = postService.userPosts(userId);
        return ResponseEntity.ok(personalPostsResponse);
    }

    @GetMapping("/posts/recommend")
    public ResponseEntity<PostRecommendsResponse> recommendedPosts(@Auth Long userId) {
        PostRecommendsResponse recommendedPost = postService.findRecommendedPost(userId);
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
    public ResponseEntity<PostSearchResponse> searchPost(@RequestParam(name = "categoryName", required = true) String categoryName,
        @RequestParam(name = "userNickname", required = false) String userNickname,
        @DateTimeFormat(iso = ISO.DATE_TIME) @RequestParam(name = "startAt", required = false) LocalDateTime startAt,
        @DateTimeFormat(iso = ISO.DATE_TIME) @RequestParam(name = "endAt", required = false) LocalDateTime endAt) {
        validateSearchParameters(userNickname, startAt, endAt);
        PostSearchRequest postSearchRequest = new PostSearchRequest(userNickname, startAt, endAt, Categories.castCategoryNameToCategoryId(categoryName));
        log.info("postSearchRequest: {}", postSearchRequest);
        PostSearchResponse postSearchResponse = postService.searchPosts(postSearchRequest);
        return ResponseEntity.ok(postSearchResponse);
    }

    private void validateSearchParameters(String userNickname, LocalDateTime startAt, LocalDateTime endAt) {
        if (userNickname == null && startAt == null && endAt == null) {
            throw new IllegalArgumentException("모든 조건이 null입니다.");
        }
        validateDateRange(startAt, endAt);
    }

    private void validateDateRange(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt == null && endAt == null) {
            return;
        }

        if (startAt.isAfter(endAt)) {
            throw new IllegalArgumentException("입력한 시간이 조건에 맞지 않습니다.");
        }
    }

    private void validateRequestDate(LocalDateTime givenDate) {
        if (ChronoUnit.SECONDS.between(LocalDateTime.now(), givenDate) > validateDifferenceOfTwoLocalDateTimeSeconds) {
            throw new IllegalArgumentException("입력된 날짜가 조건에 부합하지 않습니다.");
        }
    }

}
