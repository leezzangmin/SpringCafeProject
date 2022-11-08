package com.zzangmin.gesipan.layer.notification.dto.notification;

import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import com.zzangmin.gesipan.layer.login.entity.Users;
import com.zzangmin.gesipan.layer.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class NotificationCreateRequest {

    private final Users targetUser;
    private final Users publishedUser;
    private final Post referencePost;
    private final NotificationType notificationType;
    private final String notificationMessage;
}
