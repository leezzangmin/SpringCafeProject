package com.zzangmin.gesipan.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.*;

import java.time.LocalDateTime;


@ToString(exclude = {"postImages", "postCategory"})
@Getter
@Builder
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
    @Column(nullable = true)
    private Integer recommendCount;
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
    @Column(nullable = true)
    private Long hitCount;

}
