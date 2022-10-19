package com.zzangmin.gesipan.layer.basiccrud.entity;

import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
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
    private LocalDateTime updatedAt;

    public void update(String userName, String userNickname, LocalDateTime updateTime) {
        this.userName = userName;
        this.userNickname = userNickname;
        this.updatedAt = updateTime;
    }
}
