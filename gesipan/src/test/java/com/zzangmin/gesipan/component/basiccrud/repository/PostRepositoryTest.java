package com.zzangmin.gesipan.component.basiccrud.repository;

import com.zzangmin.gesipan.component.basiccrud.dto.post.PostSimpleQueryDTO;
import com.zzangmin.gesipan.component.basiccrud.entity.Post;
import com.zzangmin.gesipan.component.basiccrud.entity.PostCategory;
import com.zzangmin.gesipan.component.login.entity.Users;
import com.zzangmin.gesipan.component.login.repository.UsersRepository;
import com.zzangmin.gesipan.testfactory.EntityFactory;
import javax.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
@Transactional
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
        List<PostSimpleQueryDTO> posts = postRepository.paginationByPostIds(postIds);
        //then
        Assertions.assertThat(posts.get(0).getPostId()).isEqualTo(post0.getPostId());
        Assertions.assertThat(posts.get(0).getPostSubject()).isEqualTo(post0.getPostSubject());

        Assertions.assertThat(posts.get(1).getPostId()).isEqualTo(post1.getPostId());
        Assertions.assertThat(posts.get(1).getPostSubject()).isEqualTo(post1.getPostSubject());

        Assertions.assertThat(posts.get(2).getPostId()).isEqualTo(post2.getPostId());
        Assertions.assertThat(posts.get(2).getPostSubject()).isEqualTo(post2.getPostSubject());

        Assertions.assertThat(posts.get(3).getPostId()).isEqualTo(post3.getPostId());
        Assertions.assertThat(posts.get(3).getPostSubject()).isEqualTo(post3.getPostSubject());

        Assertions.assertThat(posts.get(4).getPostId()).isEqualTo(post4.getPostId());
        Assertions.assertThat(posts.get(4).getPostSubject()).isEqualTo(post4.getPostSubject());

        Assertions.assertThat(posts.get(5).getPostId()).isEqualTo(post5.getPostId());
        Assertions.assertThat(posts.get(5).getPostSubject()).isEqualTo(post5.getPostSubject());

        Assertions.assertThat(posts.get(6).getPostId()).isEqualTo(post6.getPostId());
        Assertions.assertThat(posts.get(6).getPostSubject()).isEqualTo(post6.getPostSubject());
    }


}
