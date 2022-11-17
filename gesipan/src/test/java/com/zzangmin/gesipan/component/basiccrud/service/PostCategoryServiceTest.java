package com.zzangmin.gesipan.component.basiccrud.service;

import com.zzangmin.gesipan.component.basiccrud.entity.Categories;
import com.zzangmin.gesipan.component.basiccrud.entity.PostCategory;
import com.zzangmin.gesipan.component.basiccrud.repository.PostCategoryRepository;
import com.zzangmin.gesipan.testfactory.EntityFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostCategoryServiceTest {

    @Autowired PostCategoryService postCategoryService;
    @Autowired PostCategoryRepository postCategoryRepository;

    @DisplayName("존재하지 않는 postCategoryId로 조회를 요청하면 오류가 발생해야 한다.")
    @Test
    void findOne_invalidPostCategoryId() {
        //given
        Long invalidPostCategoryId = 123456789099999L;
        //when
        //then
        Assertions.assertThatThrownBy(() -> postCategoryService.findOne(invalidPostCategoryId));
    }

    @DisplayName("정상 postCategoryId로 조회를 요청하면 PostCategory를 반환해야한다.")
    @Test
    void findOne() {
        //given
        PostCategory postCategory = EntityFactory.generatePostCategoryObject(Categories.FREE);
        postCategoryRepository.save(postCategory);
        Long postCategoryId = postCategory.getPostCategoryId();
        //when
        PostCategory findPostCategory = postCategoryService.findOne(postCategoryId);
        //then
        Assertions.assertThat(postCategory.getPostCategoryId()).isEqualTo(findPostCategory.getPostCategoryId());
        Assertions.assertThat(postCategory.getCategoryName()).isEqualTo(findPostCategory.getCategoryName());
    }
}
