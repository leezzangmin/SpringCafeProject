package com.zzangmin.gesipan.component.notification.dto.notification;

import com.zzangmin.gesipan.component.notification.entity.Notification;
import com.zzangmin.gesipan.component.notification.entity.NotificationType;
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

    private final int notificationCount;
    private final List<SingleNotification> notifications;

    @Builder(access = AccessLevel.PRIVATE)
    @Getter
    static class SingleNotification {
        private final long notificationId;
        private final long publishedUserId;
        private final long referencePostId;
        private final NotificationType notificationType;
        private final String notificationMessage;
        private final LocalDateTime createdAt;
        private final boolean isCheckedFlag;
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
