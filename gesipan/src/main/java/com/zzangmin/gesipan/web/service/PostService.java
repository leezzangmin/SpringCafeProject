package com.zzangmin.gesipan.web.service;

import com.zzangmin.gesipan.dao.*;
import com.zzangmin.gesipan.web.dto.post.*;
import com.zzangmin.gesipan.web.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final PostRecommendRepository postRecommendRepository;
    private final CommentRepository commentRepository;

    public Post findOne(Long postId) {
        Post post = postRepository.findByIdWithUser(postId).
                orElseThrow(() -> new IllegalArgumentException("해당하는 postId가 없습니다. 잘못된 입력"));
        return post;
    }

    public Long save(PostSaveRequest postSaveRequest) {
        Users user = userRepository.findById(postSaveRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 userId가 없습니다"));
        PostCategory postCategory = postCategoryRepository.findById(postSaveRequest.getPostCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 postCategoryId가 없습니다. 게시판 없음"));

        Post post = Post.builder()
                .postSubject(postSaveRequest.getPostSubject())
                .postContent(postSaveRequest.getPostContent())
                .user(user)
                .postCategory(postCategory)
                .createdAt(postSaveRequest.getCreatedAt())
                .updatedAt(postSaveRequest.getCreatedAt())
                .hitCount(0L) // TODO: DB 디폴트값 만들고 해당 줄 지우기
                .build();

        return postRepository.save(post).getPostId();
    }

    public void delete(Long postId) {
        postRepository.findById(postId).
                orElseThrow(() -> new IllegalArgumentException("해당하는 postId가 없습니다. 잘못된 입력"));

        postRepository.deleteById(postId);
    }

    public void update(Long postId, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(postId).
                orElseThrow(() -> new IllegalArgumentException("해당하는 postId가 없습니다. 잘못된 입력"));
        post.update(postUpdateRequest.getPostSubject(), postUpdateRequest.getPostContent(), postUpdateRequest.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    public PostsPageResponse pagination(Long categoryId, Pageable pageable) {
        List<Post> posts = postRepository.findPageByCategoryId(categoryId, pageable);
        List<Integer> recommendCount = postRecommendRepository.countAllByPostId(posts.stream()
                .map(i -> i.getPostId())
                .collect(Collectors.toList()));
        List<Integer> commentCounts = commentRepository.countByIds(posts.stream().map(Post::getPostId).collect(Collectors.toList()));
        return PostsPageResponse.of(categoryId, posts, recommendCount, commentCounts);
    }

    // TODO: (post_id, user_id) 복합인덱스 생성하기
    public void postRecommendCount(PostRecommendRequest postRecommendRequest) {
        Post post = postRepository.findById(postRecommendRequest.getPostId()).
                orElseThrow(() -> new IllegalArgumentException("해당하는 postId가 없습니다. 잘못된 입력"));
        Users user = userRepository.findById(postRecommendRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 userId가 없습니다"));

        postRecommendRepository.findByUsersIdAndPostId(post.getPostId(), user.getUserId())
                .ifPresent(i -> {throw new IllegalStateException("해당 유저가 이미 추천한 게시물입니다.");});

        PostRecommend postRecommend = PostRecommend.builder()
                .post(post)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        postRecommendRepository.save(postRecommend);
    }

    @Transactional(readOnly = true)
    public PersonalPostsResponse userPosts(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 userId가 없습니다"));
        List<Post> personalPosts = postRepository.findByUserId(userId);
        List<Integer> recommendCount = postRecommendRepository.countAllByPostId(personalPosts.stream()
                .map(i -> i.getPostId())
                .collect(Collectors.toList()));
        List<Integer> commentCounts = commentRepository.countByIds(personalPosts.stream()
                .map(Post::getPostId)
                .collect(Collectors.toList()));
        return PersonalPostsResponse.of(user, personalPosts, recommendCount, commentCounts);
    }

}
