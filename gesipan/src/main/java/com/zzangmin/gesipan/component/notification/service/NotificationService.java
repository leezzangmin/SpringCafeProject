package com.zzangmin.gesipan.component.notification.service;


import com.zzangmin.gesipan.component.basiccrud.entity.Post;
import com.zzangmin.gesipan.component.login.entity.Users;
import com.zzangmin.gesipan.component.login.service.UsersService;
import com.zzangmin.gesipan.component.notification.entity.NotificationType;
import com.zzangmin.gesipan.component.notification.repository.NotificationRepository;
import com.zzangmin.gesipan.component.notification.dto.notification.NotificationsResponse;
import com.zzangmin.gesipan.component.notification.entity.Notification;
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
    private final UsersService usersService;

    @Transactional(readOnly = true)
    public NotificationsResponse findAll(Long userId) {
        Users user = usersService.findOne(userId);
        List<Notification> notifications = notificationRepository.findAllByUserId(userId);
        return NotificationsResponse.of(notifications);
    }


    @Transactional
    public void createNotification(Users user, Post post, NotificationType type, LocalDateTime createdAt) {
        if (user.getUserId().equals(post.getUser().getUserId())) {
            return;
        }
        Notification notification = Notification.builder()
                .targetUser(post.getUser())
                .publishedUser(user)
                .notificationType(type)
                .referencePost(post)
                .notificationMessage(null)
                .createdAt(createdAt)
                .build();
        notificationRepository.save(notification);
    }

    @Transactional
    public void checkAll(Long userId) {
        Users user = usersService.findOne(userId);
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
