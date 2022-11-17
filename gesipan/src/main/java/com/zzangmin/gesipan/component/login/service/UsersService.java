package com.zzangmin.gesipan.component.login.service;

import com.zzangmin.gesipan.component.login.repository.UsersRepository;
import com.zzangmin.gesipan.component.login.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public Users findOneByEmail(String userEmail) {
        return usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 email 입니다."));
    }

    @Transactional(readOnly = true)
    public Users findOne(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId"));
    }

}
