package com.zzangmin.gesipan.dao;

import com.zzangmin.gesipan.web.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.user where c.post.postId=:postId")
    List<Comment> findAllByPostId(@Param("postId") Long postId);
}
