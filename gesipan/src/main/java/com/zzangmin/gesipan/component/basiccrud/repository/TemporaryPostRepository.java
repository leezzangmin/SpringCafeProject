package com.zzangmin.gesipan.component.basiccrud.repository;

import com.zzangmin.gesipan.component.basiccrud.entity.TemporaryPost;
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
