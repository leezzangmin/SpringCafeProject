package com.zzangmin.gesipan.layer.basiccrud.repository;

import com.zzangmin.gesipan.layer.basiccrud.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.user where c.post.postId=:postId")
    List<Comment> findAllByPostId(@Param("postId") Long postId);

    @Query("select count(c.post.postId) from Post p left join Comment c on p.postId = c.post.postId where p.postId in :postIds group by p.postId")
    List<Integer> countByIds(@Param("postIds") List<Long> postIds);

    @Query("select c from Comment c join fetch c.post where c.user.userId=:userId")
    List<Comment> findAllByUserIdWithPostId(@Param("userId") Long userId);

    @Query("select c from Comment c join fetch c.user where c.commentId=:commentId and c.user.userId=:userId")
    Optional<Comment> findByIdWithUsers(@Param("commentId") Long commentId, @Param("userId") Long userId);

    @Query("select c.commentId from Comment c where c.post.postId=:postId")
    List<Long> findCommentIdsByPaginationByPostId(@Param("postId") Long postId, Pageable pageable);

    @Query("select c from Comment c join fetch c.user where c.commentId in :commentIds")
    List<Comment> findByIdsWithUsers(@Param("commentIds") List<Long> commentIds);
}
