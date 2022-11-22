package com.zzangmin.gesipan.component.login.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.zzangmin.gesipan.component.BaseIntegrationTest;
import com.zzangmin.gesipan.component.login.entity.Users;
import com.zzangmin.gesipan.component.login.repository.UsersRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

class LoginControllerTest extends BaseIntegrationTest {


    @Autowired
    UsersRepository usersRepository;

    @DisplayName("로그인을 수행하면 쿠키가 반환되어야 한다.")
    @Test
    void callbackAndLoginProcess() throws Exception {
        //given
        String code = "testCode";
        //when
        ResultActions result = mvc.perform(get("/login").param("code", code));
        //then
        result
            .andExpect(status().isOk())
            .andExpect(cookie().exists("X-AUTH-TOKEN"));
    }

    @DisplayName("로그인을 수행하면 유저가 저장되어있어야 한다.")
    @Test
    void callbackAndLoginProcess_userSave() throws Exception {
        //given
        String code = "testCode";
        //when
        mvc.perform(get("/login").param("code", code));
        //then
        List<Users> all = usersRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);

    }
}
