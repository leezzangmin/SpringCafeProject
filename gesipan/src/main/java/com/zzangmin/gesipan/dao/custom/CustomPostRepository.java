package com.zzangmin.gesipan.dao.custom;

import com.zzangmin.gesipan.web.dto.post.PostSearchRequest;
import com.zzangmin.gesipan.web.entity.Post;
import java.util.List;

public interface CustomPostRepository {
    List<Post> searchPostsWithUser(PostSearchRequest postSearchRequest);
}
