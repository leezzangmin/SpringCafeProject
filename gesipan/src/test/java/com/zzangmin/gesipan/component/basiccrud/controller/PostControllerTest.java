package com.zzangmin.gesipan.component.basiccrud.controller;

import com.zzangmin.gesipan.component.BaseIntegrationTest;
import com.zzangmin.gesipan.component.basiccrud.entity.Post;
import com.zzangmin.gesipan.component.basiccrud.entity.PostCategory;
import com.zzangmin.gesipan.component.basiccrud.repository.PostCategoryRepository;
import com.zzangmin.gesipan.component.basiccrud.repository.PostRepository;
import com.zzangmin.gesipan.component.login.entity.Users;
import com.zzangmin.gesipan.component.login.repository.UsersRepository;
import com.zzangmin.gesipan.testfactory.EntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends BaseIntegrationTest {

    @Autowired
    UsersRepository usersRepository;
    @Autowired
    PostCategoryRepository postCategoryRepository;
    @Autowired
    PostRepository postRepository;

    @DisplayName("postId를 담아 요청하면 PostResponse를 반환해야 한다.")
    @Test
    void singlePost() throws Exception {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        PostCategory postCategory = post.getPostCategory();
        usersRepository.save(user);
        postCategoryRepository.save(postCategory);
        postRepository.save(post);
        //when
        ResultActions result = mvc.perform(get("/post/" + post.getPostId()));
        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getPostId()))
                .andExpect(jsonPath("$.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.userNickname").value(user.getUserNickname()))
                .andExpect(jsonPath("$.hitCount").value(post.getHitCount()))
                .andExpect(jsonPath("$.recommendCount").value(0))
                .andExpect(jsonPath("$.postSubject").value(post.getPostSubject()))
                .andExpect(jsonPath("$.postContent").value(post.getPostContent()));
    }
}
