package com.zzangmin.gesipan.layer.notification.service;


import com.zzangmin.gesipan.layer.notification.repository.NotificationRepository;
import com.zzangmin.gesipan.layer.notification.dto.notification.NotificationCreateRequest;
import com.zzangmin.gesipan.layer.notification.dto.notification.NotificationsResponse;
import com.zzangmin.gesipan.layer.notification.entity.Notification;
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

    @Transactional
    public void checkAll(Long userId) {
        notificationRepository.checkByUserId(userId, LocalDateTime.now());
    }

    @Transactional
    public void checkOne(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림번호입니다."));
        if (!userId.equals(notification.getTargetUser().getUserId())) {
            throw new IllegalArgumentException("해당 유저의 알림이 아닙니다.");
        }

        notification.updateCheckedAt(LocalDateTime.now());
    }
}
