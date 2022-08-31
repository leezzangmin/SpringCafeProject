package com.zzangmin.gesipan.web.controller;

import com.zzangmin.gesipan.dao.PostRecommendRepository;
import com.zzangmin.gesipan.web.argumentresolver.Auth;
import com.zzangmin.gesipan.web.dto.post.*;
import com.zzangmin.gesipan.web.dto.temporarypost.TemporaryPostLoadResponse;
import com.zzangmin.gesipan.web.dto.temporarypost.TemporaryPostSaveRequest;
import com.zzangmin.gesipan.web.entity.Categories;
import com.zzangmin.gesipan.web.entity.Comment;
import com.zzangmin.gesipan.web.entity.Post;
import com.zzangmin.gesipan.web.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final int validateSeconds = 60;

    private final PostService postService;
    private final CommentService commentService;
    private final PostRecommendRepository postRecommendRepository;
    private final RedisService redisService;
    private final TemporaryPostService temporaryPostService;


    // TODO: 서블릿에서 아이피 뽑아서 레디스에 캐싱하는거 말고 JWT로 구분해서 넣는것은 어떤지
    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponse> singlePost(@PathVariable Long postId, HttpServletRequest httpServletRequest) {
        log.info("postId: {}", postId);
        String clientAddress = httpServletRequest.getRemoteAddr();

        Post post = postService.findOne(postId);
        int recommendCount = postRecommendRepository.countByPostId(postId);
        List<Comment> comments = commentService.findByPostId(postId);
        redisService.increasePostHitCount(clientAddress, postId);

        return ResponseEntity.ok(PostResponse.of(post, comments, recommendCount));
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
        postService.postRecommendCount(postRecommendRequest);
        return ResponseEntity.ok("recommend success");
    }

    // TODO: @RequestParam으로 받는 userId 추후 개선 -> ArgumentResolver 쓰기;;
    @GetMapping("/posts/my")
    public ResponseEntity<PersonalPostsResponse> myPosts(@Auth Long userId) {
        PersonalPostsResponse personalPostsResponse = postService.userPosts(userId);
        return ResponseEntity.ok(personalPostsResponse);
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
    public ResponseEntity searchPost(@RequestParam(name = "categoryName", required = true) String categoryName,
        @RequestParam(name = "userNickname", required = false) String userNickname,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam(name = "startAt", required = false) LocalDateTime startAt,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam(name = "endAt", required = false) LocalDateTime endAt) {
        validateDateRange(startAt, endAt);
        if (startAt != null && endAt != null) {
            validateDateRange(startAt, endAt);
        }
        PostSearchRequest postSearchRequest = new PostSearchRequest(userNickname, startAt, endAt, Categories.castCategoryNameToCategoryId(categoryName));
        log.info("postSearchRequest: {}", postSearchRequest);
        postService.searchPosts(postSearchRequest);

        return ResponseEntity.ok(null);
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
        if (ChronoUnit.SECONDS.between(LocalDateTime.now(), givenDate) > validateSeconds) {
            throw new IllegalArgumentException("입력된 날짜가 조건에 부합하지 않습니다.");
        }
    }

}
