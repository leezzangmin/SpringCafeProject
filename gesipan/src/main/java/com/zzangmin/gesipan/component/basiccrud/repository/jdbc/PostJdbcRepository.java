package com.zzangmin.gesipan.component.basiccrud.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public int bulkUpdatePostHitCounts(String updateRows) {
        String SQL = "update post p1 "
                + " inner join "
                + " (values "
                + updateRows
                + " temptable_post (post_id, hit_count) "
                + " on p1.post_id = temptable_post.post_id "
                + " set p1.hit_count=p1.hit_count+temptable_post.hit_count";

        return jdbcTemplate.update(SQL);
    }

}
