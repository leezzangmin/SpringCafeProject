package com.zzangmin.gesipan.web.entity;

import com.zzangmin.gesipan.web.entity.entityenum.NotificationType;
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
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id", referencedColumnName = "user_id", nullable = false)
    private Users targetUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "published_user_id", referencedColumnName = "user_id", nullable = false)
    private Users publishedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_post_id", nullable = false)
    private Post referencePost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    private String notificationMessage;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime checkedAt;

}
