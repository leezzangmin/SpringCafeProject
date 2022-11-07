package com.zzangmin.gesipan.layer.login.entity;

import javax.persistence.*;

import com.zzangmin.gesipan.layer.embeddable.BaseTime;
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
    @Embedded
    private BaseTime baseTime;

    public void update(String userName, String userNickname, LocalDateTime updateTime) {
        this.userName = userName;
        this.userNickname = userNickname;
        this.baseTime.refreshUpdatedAt(updateTime);
    }
}
