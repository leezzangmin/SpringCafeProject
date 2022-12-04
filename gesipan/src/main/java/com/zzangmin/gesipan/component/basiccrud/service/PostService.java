package com.zzangmin.gesipan.component.basiccrud.service;

import com.zzangmin.gesipan.component.basiccrud.dto.comment.CommentResponse;
import com.zzangmin.gesipan.component.basiccrud.dto.post.*;
import com.zzangmin.gesipan.component.basiccrud.entity.*;
import com.zzangmin.gesipan.component.basiccrud.repository.*;
import com.zzangmin.gesipan.component.basiccrud.repository.custom.CustomPostRepository;
import java.util.Optional;

import com.zzangmin.gesipan.component.embeddable.BaseTime;
import com.zzangmin.gesipan.component.login.entity.Users;
import com.zzangmin.gesipan.component.login.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final CustomPostRepository customPostRepository;
    private final PostRecommendRepository postRecommendRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final TemporaryPostService temporaryPostService;
    private final UsersService usersService;
    private final PostCategoryService postCategoryService;

    @Cacheable(value = "single-post", key = "#postId", cacheManager = "cacheManager")
    @Transactional(readOnly = true)
    public PostResponse findOne(Long postId, Optional<Long> userId) {
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 postId가 없습니다. 잘못된 입력"));
        List<CommentResponse> commentResponses = commentService.pagination(postId, PageRequest.of(0, 10));
        int recommendCount = postRecommendRepository.countByPostId(postId);
        boolean isRecommendedFlag = isUserRecommendedPost(postId, userId);
        return PostResponse.of(post, commentResponses, recommendCount, isRecommendedFlag);
    }

    @Transactional
    public Long save(Long userId, PostSaveRequest postSaveRequest) {
        Users user = usersService.findOne(userId);
        PostCategory postCategory =postCategoryService.findOne(postSaveRequest.getPostCategoryId());

        Post post = Post.builder()
                .postSubject(postSaveRequest.getPostSubject())
                .postContent(postSaveRequest.getPostContent())
                .user(user)
                .postCategory(postCategory)
                .baseTime(new BaseTime(postSaveRequest.getCreatedAt(), postSaveRequest.getCreatedAt()))
                .hitCount(0L) // TODO: DB 디폴트값 만들고 해당 줄 지우기
                .build();

        temporaryPostService.postTemporaryDelete(userId, postSaveRequest.getTempPostId());

        return postRepository.save(post).getPostId();
    }

    @CacheEvict(value = "single-post", key = "#postId", cacheManager = "cacheManager")
    public void delete(Long postId, Long userId) {
        Users user = usersService.findOne(userId);
        Post post = postRepository.findByIdWithUser(postId).
                orElseThrow(() -> new IllegalArgumentException("해당하는 postId가 없습니다. 잘못된 입력"));
        validatePostOwner(userId, post);
        postRepository.deleteById(postId);
    }

    @CacheEvict(value = "single-post", key = "#postId", cacheManager = "cacheManager")
    @Transactional
    public void update(Long postId, PostUpdateRequest postUpdateRequest, long userId) {
        Post post = postRepository.findByIdWithUser(postId).
                orElseThrow(() -> new IllegalArgumentException("해당하는 postId가 없습니다. 잘못된 입력"));
        validatePostOwner(userId, post);
        post.update(postUpdateRequest.getPostSubject(), postUpdateRequest.getPostContent(), postUpdateRequest.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    public PostsPageResponse pagination(Long categoryId, Pageable pageable) {
        validatePageRequestSize(pageable);
        List<Long> postIds = postRepository.findPaginationPostIdsByCategoryId(categoryId, pageable);
        List<PostSimpleQueryDTO> postsDTO = postRepository.paginationByPostIds(postIds);
        List<Integer> recommendCount = postRecommendRepository.countAllByPostId(postIds);
        List<Integer> commentCounts = commentRepository.countByIds(postIds);
        return PostsPageResponse.of(categoryId, postsDTO, recommendCount, commentCounts);
    }

    // TODO: (post_id, user_id) 복합인덱스 생성하기
    @CacheEvict(value = "single-post", key = "#postRecommendRequest.postId", cacheManager = "cacheManager")
    @Transactional
    public void postRecommend(PostRecommendRequest postRecommendRequest) {
        Post post = postRepository.findById(postRecommendRequest.getPostId()).
                orElseThrow(() -> new IllegalArgumentException("해당하는 postId가 없습니다. 잘못된 입력"));
        Users user = usersService.findOne(postRecommendRequest.getUserId());

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
    public PersonalPostsResponse userPosts(Long userId, Pageable pageable) {
        validatePageRequestSize(pageable);
        Users user = usersService.findOne(userId);

        List<Long> postIds = postRepository.findPaginationPostIdsByUserId(userId, pageable);

        List<SimpleUsersPostQueryDTO> personalPosts = postRepository.findByPostIds(postIds);

        List<Integer> recommendCount = postRecommendRepository.countAllByPostId(postIds);
        List<Integer> commentCounts = commentRepository.countByIds(postIds);

        return PersonalPostsResponse.of(user, personalPosts, recommendCount, commentCounts);
    }

    // TODO: 날짜 제한 둬야할듯, 인덱스가 안먹음
    @Transactional(readOnly = true)
    public PostSearchResponse searchPosts(PostSearchRequest postSearchRequest) {
        List<Post> posts = customPostRepository.searchPostsWithUser(postSearchRequest);
        List<Long> postIds = posts.stream().map(p -> p.getPostId()).collect(Collectors.toList());
        List<Integer> recommendCount = postRecommendRepository.countAllByPostId(postIds);
        List<Integer> commentCount = commentRepository.countByIds(postIds);
        return PostSearchResponse.of(posts, recommendCount, commentCount);
    }

    @Transactional(readOnly = true)
    public PostRecommendsResponse findRecommendedPost(Long userId, Pageable pageable) {
        validatePageRequestSize(pageable);
        Users user = usersService.findOne(userId);

        List<Long> recommendedPostIds = postRecommendRepository.findPostIdsByUsersId(userId, pageable);

        List<SimpleRecommendedPostQueryDTO> postRecommends = postRecommendRepository.findByPostIds(recommendedPostIds);

        List<Long> postIds = postRecommends.stream().map(p -> p.getPostId()).collect(Collectors.toList());
        List<Integer> recommendCount = postRecommendRepository.countAllByPostId(postIds);
        List<Integer> commentCount = commentRepository.countByIds(postIds);
        return PostRecommendsResponse.of(postRecommends, recommendCount, commentCount);
    }

    private void validatePostOwner(Long userId, Post post) {
        if (!post.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 유저의 게시물이 아닙니다.");
        }
    }

    private boolean isUserRecommendedPost(Long postId, Optional<Long> userId) {
        if (userId.isPresent() && postRecommendRepository.findByUsersIdAndPostId(postId, userId.get()).isPresent()) {
            return true;
        }
        return false;
    }


    private void validatePageRequestSize(Pageable pageable) {
        // 오류를 발생시키는 게 맞나? 그냥 100개 초과 요청하면 100개만 리턴해주는 게 비즈니스적으로 맞는 흐름일까
        if (pageable.getPageSize() > 100) {
            throw new IllegalArgumentException("가져오려는 게시글 숫자가 너무 큽니다. 100 이하로 요청해주세요.");
        }
    }
}
