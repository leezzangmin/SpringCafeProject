package com.zzangmin.gesipan.component.basiccrud.repository;

import com.zzangmin.gesipan.component.basiccrud.dto.post.SimpleRecommendedPostQueryDTO;
import com.zzangmin.gesipan.component.basiccrud.entity.PostRecommend;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRecommendRepository extends JpaRepository<PostRecommend, Long> {

    @Query("select pr from PostRecommend pr where pr.user.userId=:userId and pr.post.postId=:postId")
    Optional<PostRecommend> findByUsersIdAndPostId(@Param("postId") Long postId,@Param("userId") Long userId);

    @Query("select count(pr.postRecommendId) from Post p left join PostRecommend pr on p.postId = pr.post.postId where p.postId in :postIds group by p.postId")
    List<Integer> countAllByPostId(@Param("postIds") List<Long> postIds);

    @Query("select COALESCE(count(pr.postRecommendId), 0) from PostRecommend pr where pr.post.postId=:postId")
    int countByPostId(@Param("postId") Long postId);

    @Query("select new com.zzangmin.gesipan.component.basiccrud.dto.post.SimpleRecommendedPostQueryDTO(p.post.postId, p.post.postSubject, p.post.baseTime.createdAt, p.post.hitCount, p.post.user.userId, p.post.user.userNickname) " +
            "from PostRecommend p " +
            "where p.post.postId in :postIds")
    List<SimpleRecommendedPostQueryDTO> findByPostIds(@Param("postIds") List<Long> postIds);


    @Query("select pr.post.postId from PostRecommend pr where pr.user.userId=:userId")
    List<Long> findPostIdsByUsersId(@Param("userId") Long userId, Pageable pageable);
}
