package com.zzangmin.gesipan.web.dto.notification;

import com.zzangmin.gesipan.web.entity.Post;
import com.zzangmin.gesipan.web.entity.Users;
import com.zzangmin.gesipan.web.entity.entityenum.NotificationType;
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
