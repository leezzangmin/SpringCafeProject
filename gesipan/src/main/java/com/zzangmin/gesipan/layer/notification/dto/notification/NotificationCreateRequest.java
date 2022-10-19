package com.zzangmin.gesipan.layer.notification.dto.notification;

import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import com.zzangmin.gesipan.layer.basiccrud.entity.Users;
import com.zzangmin.gesipan.layer.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class NotificationCreateRequest {

    private Users targetUser;
    private Users publishedUser;
    private Post referencePost;
    private NotificationType notificationType;
    private String notificationMessage;
}
