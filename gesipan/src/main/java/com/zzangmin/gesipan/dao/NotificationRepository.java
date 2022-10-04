package com.zzangmin.gesipan.dao;

import com.zzangmin.gesipan.web.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n where n.targetUser.userId=:userId")
    List<Notification> findAllByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("update Notification n set n.checkedAt =:now " +
            "where n.notificationId in " +
            "(select inn.notificationId from Notification inn where inn.checkedAt is not null and inn.targetUser.userId=:userId)")
    List<Long> checkByUserId(Long userId, LocalDateTime now);
}
