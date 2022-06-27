package com.zzangmin.gesipan.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    @Column(nullable = false, length = 1000)
    private String commentContent;
    @Column(nullable = false)
    private int commentDepth;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;
    @OneToMany
    @JoinColumn(name = "reference_comment_id")
    private List<Comment> childComments;
    @OneToMany
    @JoinColumn(name = "reference_id")
    private List<Image> commentImages;

}
