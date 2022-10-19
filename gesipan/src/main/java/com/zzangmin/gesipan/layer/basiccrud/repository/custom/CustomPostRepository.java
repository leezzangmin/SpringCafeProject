package com.zzangmin.gesipan.layer.basiccrud.repository.custom;

import com.zzangmin.gesipan.layer.basiccrud.dto.post.PostSearchRequest;
import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import java.util.List;

public interface CustomPostRepository {
    List<Post> searchPostsWithUser(PostSearchRequest postSearchRequest);
}
