package com.zzangmin.gesipan.dao;

import com.zzangmin.gesipan.web.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join fetch Image i where p.postId=:postId and i.imageId=:postId")
    Optional<Post> findByPostId(Long postId);
}
