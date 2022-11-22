package com.zzangmin.gesipan.component.basiccrud.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zzangmin.gesipan.config.QuerydslConfig;
import com.zzangmin.gesipan.component.basiccrud.dto.post.PostSearchRequest;
import com.zzangmin.gesipan.component.basiccrud.entity.Categories;
import com.zzangmin.gesipan.component.basiccrud.entity.Post;
import com.zzangmin.gesipan.component.basiccrud.entity.PostCategory;
import com.zzangmin.gesipan.component.embeddable.BaseTime;
import com.zzangmin.gesipan.component.login.entity.UserRole;
import com.zzangmin.gesipan.component.login.entity.Users;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(QuerydslConfig.class)
@Transactional
class CustomPostRepositoryTest {

    @Autowired EntityManager em;
    @Autowired JPAQueryFactory queryFactory;
    @Autowired CustomPostRepository customPostRepository;

    PostCategory postCategory;
    Users user;
    Post post;

    @BeforeEach
    public void init() {
        postCategory = PostCategory.builder()
            .categoryName(Categories.FREE)
            .build();
        user = Users.builder()
            .userEmail("ckdals1234@naver.com")
            .userName("이창민")
            .userNickname("zzangmin")
            .userRole(UserRole.NORMAL)
            .baseTime(new BaseTime(LocalDateTime.of(2022,2,2,2,2), LocalDateTime.of(2022,2,2,2,2)))
            .build();
        post = Post.builder()
            .postSubject("제목")
            .postContent("내용")
            .user(user)
            .postCategory(postCategory)
            .baseTime(new BaseTime(LocalDateTime.now(), LocalDateTime.now()))
            .hitCount(0L)
            .build();
        em.persist(postCategory);
        em.persist(user);
        em.persist(post);
    }

    @DisplayName("유저닉네임과 날짜조건이 모두 null이면 카테고리ID로만 조회해야 한다.")
    @Test
    void searchPostsWithUser() {
        //given
        PostSearchRequest postSearchRequest = new PostSearchRequest(null, null, null, 1L);
        //when
        List<Post> posts = customPostRepository.searchPostsWithUser(postSearchRequest);
        //then
        Assertions.assertThat(posts.get(0)).isEqualTo(post);
    }
}
