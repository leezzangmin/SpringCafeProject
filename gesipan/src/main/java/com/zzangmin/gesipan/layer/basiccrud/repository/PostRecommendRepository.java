package com.zzangmin.gesipan.layer.basiccrud.repository;

import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import com.zzangmin.gesipan.layer.basiccrud.entity.PostRecommend;
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

    @Query("select pr.post from PostRecommend pr join fetch pr.post.user where pr.user.userId=:userId")
    List<Post> findByUsersId(@Param("userId") Long userId);
}
