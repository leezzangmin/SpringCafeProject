package com.zzangmin.gesipan.layer.basiccrud.repository;

import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import com.zzangmin.gesipan.layer.basiccrud.entity.PostCategory;
import com.zzangmin.gesipan.layer.embeddable.BaseTime;
import com.zzangmin.gesipan.layer.login.entity.UserRole;
import com.zzangmin.gesipan.layer.login.entity.Users;
import com.zzangmin.gesipan.layer.login.repository.UsersRepository;
import com.zzangmin.gesipan.layer.basiccrud.entity.Categories;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
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

    PostCategory postCategory;
    Users user;
    Post post;

    @BeforeEach
    void setUp() {
        postCategory = PostCategory.builder()
                .categoryName(Categories.FREE)
                .build();
        user = Users.builder()
                .userEmail("ckdals1234@naver.com")
                .userName("이창민")
                .userNickname("zzangmin")
                .userRole(UserRole.NORMAL)
                .createdAt(LocalDateTime.of(2022,2,2,2,2))
                .updatedAt(LocalDateTime.of(2022,2,2,2,2))
                .build();

        post = Post.builder()
                .postSubject("test제목")
                .postContent("test내용")
                .user(user)
                .postCategory(postCategory)
                .baseTime(new BaseTime(LocalDateTime.of(2022,2,2,2,2), LocalDateTime.of(2022,2,2,2,2)))
                .hitCount(0L)
                .build();
        usersRepository.save(user).getUserId();
        postCategoryRepository.save(postCategory);
        postRepository.save(post).getPostId();
    }

    @Test
    void findByIdWithUser() {
        //given
        Long userId = user.getUserId();
        Long postCategoryId = postCategory.getPostCategoryId();
        Long postId = post.getPostId();
        //when
        Post post = postRepository.findByIdWithUser(postId).get();
        //then
        org.assertj.core.api.Assertions.assertThat(post.getPostId()).isEqualTo(postId);
        org.assertj.core.api.Assertions.assertThat(post.getUser().getUserId()).isEqualTo(userId);

    }

    @Test
    void findPageByCategoryId() {
        //given
        Long userId = user.getUserId();
        Long postCategoryId = postCategory.getPostCategoryId();
        Long postId = post.getPostId();
        Pageable pageable = PageRequest.of(0,10);

        //when
        List<Long> postIds = postRepository.findPaginationPostIdsByCategoryId(postCategoryId, pageable);
        List<Post> posts = postRepository.paginationByPostIds(postIds);
        List<Long> nonCategorizedPosts = postRepository.findPaginationPostIdsByCategoryId(99999L, pageable);

        //then
        org.assertj.core.api.Assertions.assertThat(posts.size()).isLessThan(10).isGreaterThan(0);
        Assertions.assertThat(nonCategorizedPosts.size()).isEqualTo(0);
    }
}
