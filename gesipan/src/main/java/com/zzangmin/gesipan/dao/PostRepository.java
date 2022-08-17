package com.zzangmin.gesipan.dao;

import com.zzangmin.gesipan.web.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join fetch p.user where p.postId=:postId")
    Optional<Post> findByIdWithUser(@Param("postId") Long postId);

    @Query("select p from Post p join fetch p.user where p.postCategory.postCategoryId=:categoryId")
    List<Post> findPageByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("select p from Post p where p.user.userId=:userId")
    List<Post> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("update Post p set p.hitCount = p.hitCount + :hitCount where p.postId =:postId")
    void updateHitCountByPostId(@Param("postId") Long postId, @Param("hitCount") Long hitCount);
}
