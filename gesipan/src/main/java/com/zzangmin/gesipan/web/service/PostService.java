package com.zzangmin.gesipan.web.service;

import com.zzangmin.gesipan.dao.PostRepository;
import com.zzangmin.gesipan.web.dto.PostResponse;
import com.zzangmin.gesipan.web.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public PostResponse findOne(Long postId) {
        Post post = postRepository.findByPostId(postId).orElseThrow(IllegalStateException::new);

    }




}
