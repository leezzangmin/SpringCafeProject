package com.zzangmin.gesipan.dao;

import com.zzangmin.gesipan.web.entity.PostRecommend;
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
}
