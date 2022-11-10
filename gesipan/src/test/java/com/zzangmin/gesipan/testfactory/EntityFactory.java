package com.zzangmin.gesipan.testfactory;

import com.zzangmin.gesipan.layer.basiccrud.entity.*;
import com.zzangmin.gesipan.layer.embeddable.BaseTime;
import com.zzangmin.gesipan.layer.login.entity.UserRole;
import com.zzangmin.gesipan.layer.login.entity.Users;
import java.time.LocalDateTime;
import java.util.UUID;

public class EntityFactory {

    private EntityFactory() {
        throw new AssertionError();
    }

    public static Comment generateRandomCommentObject() {
        Users user = generateRandomUsersObject();
        Post post = generateRandomPostObject();

        return Comment.builder()
            .commentContent(UUID.randomUUID().toString())
            .post(post)
            .user(user)
            .baseTime(generateRandomBaseTime())
            .build();
    }

    public static Comment generateCommentObject() {
        Users user = generateRandomUsersObject();
        Post post = generateRandomPostObject();

        return Comment.builder()
            .commentContent(UUID.randomUUID().toString())
            .post(post)
            .user(user)
            .baseTime(generateFixedBaseTime())
            .build();
    }

    public static Comment generateCommentObject(Post post) {
        Users user = generateRandomUsersObject();

        return Comment.builder()
            .commentContent(UUID.randomUUID().toString())
            .post(post)
            .user(user)
            .baseTime(generateFixedBaseTime())
            .build();
    }

    public static Comment generateCommentObject(Users user) {
        Post post = generateRandomPostObject();

        return Comment.builder()
                .commentContent(UUID.randomUUID().toString())
                .post(post)
                .user(user)
                .baseTime(generateFixedBaseTime())
                .build();
    }

    public static Comment generateCommentObject(Post post, Users user) {
        return Comment.builder()
            .commentContent(UUID.randomUUID().toString())
            .post(post)
            .user(user)
            .baseTime(generateFixedBaseTime())
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
            .baseTime(generateRandomBaseTime())
            .build();
    }

    public static Post generateRandomPostObject(Users user) {
        PostCategory postCategory = generatePostCategoryObject(Categories.FREE);

        return Post.builder()
            .postSubject(generateRandomUUIDString())
            .postContent(generateRandomUUIDString())
            .user(user)
            .postCategory(postCategory)
            .hitCount(0L)
            .baseTime(generateRandomBaseTime())
            .build();
    }

    public static Post generateRandomPostObject(Users user, PostCategory postCategory) {
        return Post.builder()
                .postSubject(generateRandomUUIDString())
                .postContent(generateRandomUUIDString())
                .user(user)
                .postCategory(postCategory)
                .hitCount(0L)
                .baseTime(generateRandomBaseTime())
                .build();
    }

    public static Users generateRandomUsersObject() {
        return Users.builder()
            .userEmail(generateRandomUUIDString())
            .userName(generateRandomUUIDString().substring(0, 5))
            .userNickname(generateRandomUUIDString())
            .userRole(UserRole.NORMAL)
            .baseTime(generateRandomBaseTime())
            .build();
    }

    public static BaseTime generateRandomBaseTime() {
        LocalDateTime now = LocalDateTime.now();
        return new BaseTime(now, now);
    }

    public static BaseTime generateFixedBaseTime() {
        LocalDateTime fixed = LocalDateTime.of(2022,01, 01, 01, 01, 01);
        return new BaseTime(fixed, fixed);
    }

    public static TemporaryPost generateRandomTemporaryPostObject() {
        Users user = generateRandomUsersObject();
        return TemporaryPost.builder()
                .postSubject("temp제목")
                .postContent("temp내용")
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
    }

    public static TemporaryPost generateRandomTemporaryPostObject(Users user) {
        return TemporaryPost.builder()
                .postSubject("temp제목")
                .postContent("temp내용")
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
    }

    private static String generateRandomUUIDString() {
        return UUID.randomUUID().toString();
    }

}
