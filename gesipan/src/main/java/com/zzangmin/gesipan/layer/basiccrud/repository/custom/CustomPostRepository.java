package com.zzangmin.gesipan.layer.basiccrud.repository.custom;


import static com.zzangmin.gesipan.layer.basiccrud.entity.QPost.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zzangmin.gesipan.layer.basiccrud.dto.post.PostSearchRequest;
import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Repository
public class CustomPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Post> searchPostsWithUser(PostSearchRequest postSearchRequest) {
        JPQLQuery query = jpaQueryFactory.from(post)
            .select(post)
            .join(post.user).fetchJoin()
            .where(post.postCategory.postCategoryId.eq(postSearchRequest.getPostCategoryId()),
                eqUserNickname(postSearchRequest.getUserNickname()),
                betweenDate(postSearchRequest.getStartAt(), postSearchRequest.getEndAt()));

        return query.fetch();
    }

    private BooleanExpression eqUserNickname(String userNickname) {
        if (userNickname != null) {
            return post.user.userNickname.eq(userNickname);
        }
        return null;
    }

    private BooleanExpression betweenDate(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt != null) {

            return post.baseTime.createdAt.between(startAt, endAt);
        }
        return null;
    }

}
