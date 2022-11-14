package com.zzangmin.gesipan.layer.basiccrud.repository;

import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import com.zzangmin.gesipan.layer.basiccrud.entity.PostCategory;
import com.zzangmin.gesipan.layer.login.entity.Users;
import com.zzangmin.gesipan.layer.login.repository.UsersRepository;
import com.zzangmin.gesipan.testfactory.EntityFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    PostCategoryRepository postCategoryRepository;
    @Autowired
    UsersRepository usersRepository;


    @DisplayName("postId로 게시글+유저 조회를 수행해야 한다")
    @Test
    void findByIdWithUser() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        PostCategory postCategory = post.getPostCategory();

        usersRepository.save(user);
        postCategoryRepository.save(postCategory);
        postRepository.save(post);

        Long userId = user.getUserId();
        Long postCategoryId = postCategory.getPostCategoryId();
        Long postId = post.getPostId();
        //when
        Post findPost = postRepository.findByIdWithUser(postId).get();
        //then
        Assertions.assertThat(findPost.getPostId()).isEqualTo(postId);
        Assertions.assertThat(findPost.getUser().getUserId()).isEqualTo(userId);

    }

}
