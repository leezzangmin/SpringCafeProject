package com.zzangmin.gesipan.web.controller;

import com.zzangmin.gesipan.web.argumentresolver.Auth;
import com.zzangmin.gesipan.web.dto.notification.NotificationsResponse;
import com.zzangmin.gesipan.web.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ResponseEntity<NotificationsResponse> userNotifications(@Auth Long userId) {
        return ResponseEntity.ok(notificationService.findAll(userId));
    }

    @PostMapping("/notifications/checked")
    public ResponseEntity checkAllNotifications(@Auth Long userId) {
        notificationService.checkAll(userId);
    }

    @PostMapping("/notifications/{notificationId}/checked")
    public ResponseEntity checkOneNotification(@Auth Long userId, @PathVariable Long notificationId) {
        notificationService.checkOne(userId);
    }
}

