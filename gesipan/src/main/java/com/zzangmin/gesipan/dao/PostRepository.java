package com.zzangmin.gesipan.dao;

import com.zzangmin.gesipan.web.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join fetch p.user where p.postId=:postId")
    Optional<Post> findByIdWithUser(@Param("postId") Long postId);
}
