package com.zzangmin.gesipan.component.basiccrud.service;

import static org.junit.jupiter.api.Assertions.*;

import com.zzangmin.gesipan.component.basiccrud.dto.temporarypost.TemporaryPostSaveRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TemporaryPostServiceTest {

    @Autowired
    TemporaryPostService temporaryPostService;

    @DisplayName("존재하지 않는 userId로 임시게시물 저장을 요청하면 오류가 발생해야 한다.")
    @Test
    void postTemporarySave_invalidUserId() {
        //given
        Long invalidUserId=99999999999999L;
        TemporaryPostSaveRequest temporaryPostSaveRequest = new TemporaryPostSaveRequest("post_subject", "post_content", 1L);
        //when
        //then
        Assertions.assertThatThrownBy(() -> temporaryPostService.postTemporarySave(invalidUserId, temporaryPostSaveRequest));
    }


}
