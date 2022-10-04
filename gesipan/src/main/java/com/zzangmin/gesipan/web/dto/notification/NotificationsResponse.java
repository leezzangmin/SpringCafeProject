package com.zzangmin.gesipan.web.dto.notification;

import com.zzangmin.gesipan.web.entity.Notification;
import com.zzangmin.gesipan.web.entity.entityenum.NotificationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NotificationsResponse {

    private int notificationCount;
    private List<SingleNotification> notifications;

    @Builder(access = AccessLevel.PRIVATE)
    @Getter
    static class SingleNotification {
        private long notificationId;
        private long publishedUserId;
        private long referencePostId;
        private NotificationType notificationType;
        private String notificationMessage;
        private LocalDateTime createdAt;
        private boolean isCheckedFlag;
    }

    public static NotificationsResponse of(List<Notification> notifications) {
        return new NotificationsResponse(notifications.size(),
                notifications.stream()
                        .map(i -> SingleNotification.builder()
                                .notificationId(i.getNotificationId())
                                .publishedUserId(i.getPublishedUser().getUserId())
                                .referencePostId(i.getReferencePost().getPostId())
                                .notificationType(i.getNotificationType())
                                .notificationMessage(i.getNotificationMessage())
                                .createdAt(i.getCreatedAt())
                                .isCheckedFlag(i.getCheckedAt() != null ? true : false)
                                .build())
                        .collect(Collectors.toList())
        );
    }

}
