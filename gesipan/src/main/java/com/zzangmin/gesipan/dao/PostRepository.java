package com.zzangmin.gesipan.dao;

import com.zzangmin.gesipan.web.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Deprecated(since = "해결책을 찾으면 다시 수정하고 쓰기로!")
    @Transactional
    @Modifying
    @Query("update Post p set p.hitCount = p.hitCount + :hitCounts where p.postId in :postIds")
    void updatePostsHitCounts(@Param("postIds") List<Long> postIds, @Param("hitCounts") List<Long> hitCounts);


}
