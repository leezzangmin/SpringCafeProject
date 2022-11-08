package com.zzangmin.gesipan.testfactory;

import com.zzangmin.gesipan.layer.basiccrud.entity.Categories;
import com.zzangmin.gesipan.layer.basiccrud.entity.Comment;
import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import com.zzangmin.gesipan.layer.basiccrud.entity.PostCategory;
import com.zzangmin.gesipan.layer.embeddable.BaseTime;
import com.zzangmin.gesipan.layer.login.entity.UserRole;
import com.zzangmin.gesipan.layer.login.entity.Users;
import java.time.LocalDateTime;
import java.util.UUID;

public class EntityFactory {

    private EntityFactory() {
        throw new AssertionError();
    }

    public static Comment makeRandomCommentObject() {
        Users user = generateRandomUsersObject();
        Post post = generateRandomPostObject();

        return Comment.builder()
            .commentContent(UUID.randomUUID().toString())
            .post(post)
            .user(user)
            .baseTime(generateNOWBaseTime())
            .build();
    }

    public static PostCategory generatePostCategoryObject(Categories categories) {
        return PostCategory.builder()
            .categoryName(categories)
            .build();
    }

    public static Post generateRandomPostObject() {
        Users user = generateRandomUsersObject();
        PostCategory postCategory = generatePostCategoryObject(Categories.FREE);

        return Post.builder()
            .postSubject(generateRandomUUIDString())
            .postContent(generateRandomUUIDString())
            .user(user)
            .postCategory(postCategory)
            .hitCount(0L)
            .baseTime(generateNOWBaseTime())
            .build();
    }
    public static Users generateRandomUsersObject() {
        return Users.builder()
            .userEmail(generateRandomUUIDString())
            .userName(generateRandomUUIDString())
            .userNickname(generateRandomUUIDString())
            .userRole(UserRole.NORMAL)
            .baseTime(generateNOWBaseTime())
            .build();
    }

    public static BaseTime generateNOWBaseTime() {
        LocalDateTime now = LocalDateTime.now();
        return new BaseTime(now, now);
    }
    private static String generateRandomUUIDString() {
        return UUID.randomUUID().toString();
    }



}
