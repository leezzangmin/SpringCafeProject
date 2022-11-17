package com.zzangmin.gesipan.component.basiccrud.repository;

import com.zzangmin.gesipan.component.basiccrud.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
}
