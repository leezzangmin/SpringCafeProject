package com.zzangmin.gesipan.web.service;

import com.zzangmin.gesipan.dao.UsersRepository;
import com.zzangmin.gesipan.web.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public Users findOneByEmail(String userEmail) {
        Users user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 email 입니다."));
        return user;
    }

}
