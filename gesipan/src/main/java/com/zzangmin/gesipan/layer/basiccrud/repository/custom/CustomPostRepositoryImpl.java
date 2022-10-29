package com.zzangmin.gesipan.layer.basiccrud.repository.custom;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.zzangmin.gesipan.layer.basiccrud.dto.post.PostSearchRequest;
import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import com.zzangmin.gesipan.layer.basiccrud.entity.QPost;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.zzangmin.gesipan.layer.basiccrud.entity.QPost.post;

@Repository
public class CustomPostRepositoryImpl extends QuerydslRepositorySupport implements CustomPostRepository {

    public CustomPostRepositoryImpl() {
        super(Post.class);
    }

    @Override
    public List<Post> searchPostsWithUser(PostSearchRequest postSearchRequest) {
        QPost post = QPost.post;
        JPQLQuery query =
            from(post)
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
            return post.createdAt.between(startAt, endAt);
        }
        return null;
    }

}
