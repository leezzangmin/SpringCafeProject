package com.zzangmin.gesipan.dao;

import com.zzangmin.gesipan.web.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n where n.targetUser.userId=:userId")
    List<Notification> findAllByUserId(@Param("userId") Long userId);

}
