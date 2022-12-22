package com.zzangmin.gesipan.component.basiccrud.repository.jdbc;

import com.zzangmin.gesipan.component.basiccrud.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostJdbcRepository extends CrudRepository<Post, Long> {

    @Modifying
    @Query("update post p1 "
        + " inner join (values :updateRows "
        + " post (post_id, hit_count) p2 "
        + " on p1.post_id = p2.post_id "
        + " set p1.hit_count=p1.hit_count+p2.hit_count")
    void hitCount(@Param("updateRows") String updateRows);


}
