package com.zzangmin.gesipan.web.service;

import com.zzangmin.gesipan.dao.CommentRepository;
import com.zzangmin.gesipan.dao.PostCategoryRepository;
import com.zzangmin.gesipan.dao.PostRepository;
import com.zzangmin.gesipan.dao.UserRepository;
import com.zzangmin.gesipan.web.dto.PostResponse;
import com.zzangmin.gesipan.web.dto.PostSaveRequest;
import com.zzangmin.gesipan.web.dto.PostUpdateRequest;
import com.zzangmin.gesipan.web.dto.PostsPageResponse;
import com.zzangmin.gesipan.web.entity.Comment;
import com.zzangmin.gesipan.web.entity.Post;
import com.zzangmin.gesipan.web.entity.PostCategory;
import com.zzangmin.gesipan.web.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostCategoryRepository postCategoryRepository;

    public PostResponse findOne(Long postId) {
        Post post = postRepository.findByIdWithUser(postId).
                orElseThrow(() -> new IllegalArgumentException("해당하는 postId가 없습니다. 잘못된 입력"));
        post.increaseHitCount();
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return PostResponse.of(post, comments);
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

    public PostsPageResponse pagination(Long categoryId, int currentPageNumber, int offset) {
        List<Post> all = postRepository.findPageByCategoryId();
        return PostsPageResponse.of(categoryId, posts);
    }
}
