package com.zzangmin.gesipan.web.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Users {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 200, columnDefinition = "VARCHAR(200)")
    private String userEmail;

    @Column(nullable = false, length = 10, columnDefinition = "VARCHAR(10)")
    private String userName;
    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    private String userNickname;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45, columnDefinition = "VARCHAR(45)")
    private UserRole userRole;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime updateAt;

    @OneToMany
    @JoinColumn(name = "reference_id")
    private List<Image> userImage;
}
