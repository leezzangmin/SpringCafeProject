package com.zzangmin.gesipan.web.controller;

import com.zzangmin.gesipan.web.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostController {


    @GetMapping("/post/{postId}")
    public PostResponse singlePost(@PathVariable Long postId) {

        return new PostResponse();
    }

}
