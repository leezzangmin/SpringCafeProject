package com.zzangmin.gesipan.component.notification.repository;

import com.zzangmin.gesipan.component.notification.entity.Notification;
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
    @Query(value = "update notification as n set n.checked_at =:now where n.notification_id in (select temp.notification_id from (select notification_id from notification where checked_at is null and target_user_id=:userId) temp)", nativeQuery = true)
    void checkByUserId(@Param("userId") Long userId, @Param("now") LocalDateTime now);
}
