package com.zzangmin.gesipan.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;


@ToString(exclude = {"postImages", "postCategory"})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @Column(nullable = false, length = 1000)
    private String postSubject;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String postContent;
    @Column(nullable = false)
    private int recommendCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referenceCategoryId")
    private PostCategory postCategory;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime updatedAt;
    @Column(nullable = false)
    private Long hitCount;

}
