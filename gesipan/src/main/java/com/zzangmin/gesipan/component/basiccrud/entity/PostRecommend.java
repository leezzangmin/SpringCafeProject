package com.zzangmin.gesipan.component.basiccrud.entity;

import com.zzangmin.gesipan.component.login.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PostRecommend {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postRecommendId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @Column(nullable = false)
    private LocalDateTime createdAt;

}
