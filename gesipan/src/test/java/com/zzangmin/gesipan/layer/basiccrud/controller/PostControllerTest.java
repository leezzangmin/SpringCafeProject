package com.zzangmin.gesipan.layer.basiccrud.controller;

import com.zzangmin.gesipan.layer.basiccrud.dto.post.PostResponse;
import com.zzangmin.gesipan.layer.basiccrud.service.PostService;
import com.zzangmin.gesipan.layer.basiccrud.service.TemporaryPostService;
import com.zzangmin.gesipan.layer.caching.redis.RedisPostHitCountBulkUpdateService;
import com.zzangmin.gesipan.layer.login.service.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = PostController.class)
class PostControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    PostService postService;

    @MockBean
    RedisPostHitCountBulkUpdateService redisPostHitCountBulkUpdateService;

    @MockBean
    JwtProvider jwtProvider;

    @MockBean
    TemporaryPostService temporaryPostService;


    @Test
    @DisplayName("게시글 단건조회를 하면 PostResponse 객체 Json이 반환되어야 한다")
    void singlePost() throws Exception {
        //given
        PostResponse postResponse = new PostResponse(123L, 10L, "givenUserNickname", 0L, 0, "givenSubject", "givenContent", LocalDateTime.of(2022,2,2,2,2), LocalDateTime.of(2022,2,2,2,2), new ArrayList<>(), false);
        given(jwtProvider.getUserId(any()))
                .willReturn(Optional.empty());
        given(postService.findOne(123L, Optional.empty()))
                .willReturn(postResponse);
        //when
        mvc.perform(MockMvcRequestBuilders.get("/post/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId").value(123))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userNickname").value("givenUserNickname"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments").value(new ArrayList<>()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recommended").value(false));
        //then
    }
}