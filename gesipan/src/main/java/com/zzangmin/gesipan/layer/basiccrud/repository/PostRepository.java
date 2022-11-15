package com.zzangmin.gesipan.layer.basiccrud.repository;

import com.zzangmin.gesipan.layer.basiccrud.dto.post.PostSimpleQueryDTO;
import com.zzangmin.gesipan.layer.basiccrud.dto.post.SimpleUsersPostQueryDTO;
import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
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

    @Query("select new com.zzangmin.gesipan.layer.basiccrud.dto.post.PostSimpleQueryDTO(p.postId, p.postSubject, p.baseTime.createdAt, p.hitCount, p.user.userId, p.user.userNickname) " +
            "from Post p inner join p.user u " +
            "where p.postId in :postIds and u.userId=p.user.userId")
    List<PostSimpleQueryDTO> paginationByPostIds(@Param("postIds") List<Long> postIds);
    @Query("select new com.zzangmin.gesipan.layer.basiccrud.dto.post.SimpleUsersPostQueryDTO(p.postId, p.postSubject, p.baseTime.createdAt, p.hitCount) " +
            "from Post p " +
            "where p.user.userId=:userId")
    List<SimpleUsersPostQueryDTO> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("update Post p set p.hitCount = p.hitCount + :hitCount where p.postId =:postId")
    void updateHitCountByPostId(@Param("postId") Long postId, @Param("hitCount") Long hitCount);

    @Query("select p.postId from Post p where p.postCategory.postCategoryId=:categoryId")
    List<Long> findPaginationPostIdsByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
}
