package com.zzangmin.gesipan.web.service;


import com.zzangmin.gesipan.dao.NotificationRepository;
import com.zzangmin.gesipan.web.dto.notification.NotificationsResponse;
import com.zzangmin.gesipan.web.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationsResponse findAll(Long userId) {
        List<Notification> notifications = notificationRepository.findAllByUserId(userId);
        return NotificationsResponse.of(notifications);
    }
}
