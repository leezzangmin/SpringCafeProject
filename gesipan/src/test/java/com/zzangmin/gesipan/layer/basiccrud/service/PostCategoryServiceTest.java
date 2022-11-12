package com.zzangmin.gesipan.layer.basiccrud.service;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostCategoryServiceTest {

    @Autowired PostCategoryService postCategoryService;

    @DisplayName("존재하지 않는 postCategoryId로 조회를 요청하면 오류가 발생해야 한다.")
    @Test
    void findOne_invalidPostCategoryId() {
        //given
        Long invalidPostCategoryId = 123456789099999L;
        //when
        //then
        Assertions.assertThatThrownBy(() -> postCategoryService.findOne(invalidPostCategoryId));
    }
}
