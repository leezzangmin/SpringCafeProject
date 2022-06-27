package com.zzangmin.gesipan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
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
    @JoinColumn(name = "categoryId")
    private PostCategory postCategory;
    @OneToMany
    @JoinColumn(name = "reference_id")
    private List<Image> postImages;

}
