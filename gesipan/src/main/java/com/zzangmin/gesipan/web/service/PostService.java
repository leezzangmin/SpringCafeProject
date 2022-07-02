package com.zzangmin.gesipan.web.service;

import com.zzangmin.gesipan.dao.PostCategoryRepository;
import com.zzangmin.gesipan.dao.PostRepository;
import com.zzangmin.gesipan.dao.UserRepository;
import com.zzangmin.gesipan.web.dto.PostResponse;
import com.zzangmin.gesipan.web.dto.PostSaveRequest;
import com.zzangmin.gesipan.web.entity.Post;
import com.zzangmin.gesipan.web.entity.PostCategory;
import com.zzangmin.gesipan.web.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostCategoryRepository postCategoryRepository;

    public PostResponse findOne(Long postId) {
        return null;
    }

    @Transactional
    public Long save(PostSaveRequest postSaveRequest) {
        Users user = userRepository.findById(postSaveRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 userId가 없습니다"));
        PostCategory postCategory = postCategoryRepository.findById(postSaveRequest.getPostCategoryId())
                .orElseThrow(() -> new IllegalStateException("해당하는 postCategoryId가 없습니다. 게시판 없음"));

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

    @Transactional
    public void delete(Long postId) {
        postRepository.findById(postId).
                orElseThrow(() -> new IllegalStateException("해당하는 postId가 없습니다. 잘못된 입력"));

        postRepository.deleteById(postId);
    }
}
