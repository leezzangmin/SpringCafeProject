package com.zzangmin.gesipan.web.controller;

import com.zzangmin.gesipan.dao.PostRecommendRepository;
import com.zzangmin.gesipan.web.dto.post.*;
import com.zzangmin.gesipan.web.entity.Categories;
import com.zzangmin.gesipan.web.entity.Comment;
import com.zzangmin.gesipan.web.entity.Post;
import com.zzangmin.gesipan.web.entity.Users;
import com.zzangmin.gesipan.web.jwt.JwtProvider;
import com.zzangmin.gesipan.web.service.CommentService;
import com.zzangmin.gesipan.web.service.PostService;
import com.zzangmin.gesipan.web.service.RedisService;
import com.zzangmin.gesipan.web.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    private final JwtProvider jwtProvider;
    private final UsersService usersService;

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

    @PostMapping("/post")
    public ResponseEntity<Long> createPost(@RequestBody @Valid PostSaveRequest postSaveRequest) {
        log.info("post create: {}", postSaveRequest);
        validateRequestDate(postSaveRequest.getCreatedAt());
        return ResponseEntity.ok(postService.save(postSaveRequest));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> removePost(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.ok("post remove success");
    }

    @PatchMapping("/post/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody @Valid PostUpdateRequest postUpdateRequest) {
        log.info("post update :{}", postUpdateRequest);
        validateRequestDate(postUpdateRequest.getUpdatedAt());
        postService.update(postId, postUpdateRequest);
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
    public ResponseEntity<PersonalPostsResponse> myPosts(HttpServletRequest request) {
        String jwt = jwtProvider.resolveToken(request);
        String userInfo = jwtProvider.getUserInfo(jwt);
        Users user = usersService.findOneByEmail(userInfo);

        PersonalPostsResponse personalPostsResponse = postService.userPosts(user.getUserId());
        return ResponseEntity.ok(personalPostsResponse);
    }

    private void validateRequestDate(LocalDateTime givenDate) {
        if (ChronoUnit.SECONDS.between(LocalDateTime.now(), givenDate) > validateSeconds) {
            throw new IllegalArgumentException("입력된 날짜가 조건에 부합하지 않습니다.");
        }
    }

}
