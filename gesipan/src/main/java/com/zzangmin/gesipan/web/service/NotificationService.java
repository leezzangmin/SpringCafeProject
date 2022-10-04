package com.zzangmin.gesipan.web.service;


import com.zzangmin.gesipan.dao.NotificationRepository;
import com.zzangmin.gesipan.web.dto.notification.NotificationCreateRequest;
import com.zzangmin.gesipan.web.dto.notification.NotificationsResponse;
import com.zzangmin.gesipan.web.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public NotificationsResponse findAll(Long userId) {
        List<Notification> notifications = notificationRepository.findAllByUserId(userId);
        return NotificationsResponse.of(notifications);
    }

    @Transactional
    public long createNotification(NotificationCreateRequest request) {
        Notification notification = Notification.builder()
                .targetUser(request.getTargetUser())
                .publishedUser(request.getPublishedUser())
                .notificationType(request.getNotificationType())
                .referencePost(request.getReferencePost())
                .notificationMessage(request.getNotificationMessage())
                .createdAt(LocalDateTime.now())
                .build();
        return notificationRepository.save(notification).getNotificationId();
    }

    public void checkAll(Long userId) {
        notificationRepository.checkByUserId(userId, LocalDateTime.now());
    }
}
