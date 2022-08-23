package com.zzangmin.gesipan.dao;

import com.zzangmin.gesipan.web.entity.TemporaryPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemporaryPostRepository extends JpaRepository<TemporaryPost, Long> {

    @Query("select pt from TemporaryPost pt where pt.user.userId=:userId")
    List<TemporaryPost> findByUserId(@Param("userId") Long userId);
}
