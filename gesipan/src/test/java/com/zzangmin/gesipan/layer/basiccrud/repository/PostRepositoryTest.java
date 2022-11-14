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

import java.util.List;


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

    @DisplayName("postIds로 게시글을 조회해야 한다.")
    @Test
    void paginationByPostIds() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post0 = EntityFactory.generateRandomPostObject(user);
        Post post1 = EntityFactory.generateRandomPostObject(user, post0.getPostCategory());
        Post post2 = EntityFactory.generateRandomPostObject(user, post0.getPostCategory());
        Post post3 = EntityFactory.generateRandomPostObject(user, post0.getPostCategory());
        Post post4 = EntityFactory.generateRandomPostObject(user, post0.getPostCategory());
        Post post5 = EntityFactory.generateRandomPostObject(user, post0.getPostCategory());
        Post post6 = EntityFactory.generateRandomPostObject(user, post0.getPostCategory());

        usersRepository.save(user);
        postCategoryRepository.save(post1.getPostCategory());
        postRepository.save(post0);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);
        postRepository.save(post5);
        postRepository.save(post6);

        List<Long> postIds = List.of(post0.getPostId(), post1.getPostId(),post2.getPostId(),post3.getPostId(),post4.getPostId(),post5.getPostId(),post6.getPostId());

        //when
        List<Post> posts = postRepository.paginationByPostIds(postIds);
        //then
        Assertions.assertThat(posts.get(0).getPostId()).isEqualTo(post0.getPostId());
        Assertions.assertThat(posts.get(0).getPostContent()).isEqualTo(post0.getPostContent());

        Assertions.assertThat(posts.get(1).getPostId()).isEqualTo(post1.getPostId());
        Assertions.assertThat(posts.get(1).getPostContent()).isEqualTo(post1.getPostContent());

        Assertions.assertThat(posts.get(2).getPostId()).isEqualTo(post2.getPostId());
        Assertions.assertThat(posts.get(2).getPostContent()).isEqualTo(post2.getPostContent());

        Assertions.assertThat(posts.get(3).getPostId()).isEqualTo(post3.getPostId());
        Assertions.assertThat(posts.get(3).getPostContent()).isEqualTo(post3.getPostContent());

        Assertions.assertThat(posts.get(4).getPostId()).isEqualTo(post4.getPostId());
        Assertions.assertThat(posts.get(4).getPostContent()).isEqualTo(post4.getPostContent());

        Assertions.assertThat(posts.get(5).getPostId()).isEqualTo(post5.getPostId());
        Assertions.assertThat(posts.get(5).getPostContent()).isEqualTo(post5.getPostContent());

        Assertions.assertThat(posts.get(6).getPostId()).isEqualTo(post6.getPostId());
        Assertions.assertThat(posts.get(6).getPostContent()).isEqualTo(post6.getPostContent());
    }


}
