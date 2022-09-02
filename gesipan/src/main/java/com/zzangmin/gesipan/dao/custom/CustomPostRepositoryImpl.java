package com.zzangmin.gesipan.dao.custom;

import static com.zzangmin.gesipan.web.entity.QPost.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.zzangmin.gesipan.web.dto.post.PostSearchRequest;
import com.zzangmin.gesipan.web.entity.Post;
import com.zzangmin.gesipan.web.entity.QPost;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

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
