package com.zzangmin.gesipan.web.service;

import com.zzangmin.gesipan.dao.PostRepository;
import com.zzangmin.gesipan.web.dto.PostResponse;
import com.zzangmin.gesipan.web.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public PostResponse findOne(Long postId) {
//        postRepository.save(new Post(1L, "asdf", "asdf", 0, null, new PostCategory(),null));
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new IllegalStateException("포스트id 없음"));
        System.out.println(post);
        return null;
    }
}
