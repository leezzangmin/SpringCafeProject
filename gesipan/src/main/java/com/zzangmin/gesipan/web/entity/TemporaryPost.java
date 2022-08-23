package com.zzangmin.gesipan.web.entity;

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
@Table(name = "temp_post")
@Entity
public class TemporaryPost {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tempPostId;
    @ManyToOne
    @JoinColumn(name = "reference_user_id")
    private Users user;
    @Column(columnDefinition = "VARCHAR(1000)", nullable = false)
    private String postSubject;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String postContent;
    private LocalDateTime createdAt;

}
