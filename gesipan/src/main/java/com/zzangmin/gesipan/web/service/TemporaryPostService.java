package com.zzangmin.gesipan.web.service;

import com.zzangmin.gesipan.dao.TemporaryPostRepository;
import com.zzangmin.gesipan.dao.UsersRepository;
import com.zzangmin.gesipan.web.dto.temporarypost.TemporaryPostLoadResponse;
import com.zzangmin.gesipan.web.dto.temporarypost.TemporaryPostSaveRequest;
import com.zzangmin.gesipan.web.entity.TemporaryPost;
import com.zzangmin.gesipan.web.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TemporaryPostService {

    private final TemporaryPostRepository temporaryPostRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void postTemporarySave(Long userId, TemporaryPostSaveRequest temporaryPostSaveRequest) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 userId 입니다."));

        TemporaryPost temporaryPost = TemporaryPost.builder()
                .postSubject(temporaryPostSaveRequest.getPostSubject())
                .postContent(temporaryPostSaveRequest.getPostContent())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        temporaryPostRepository.save(temporaryPost);
    }

    @Transactional(readOnly = true)
    public TemporaryPostLoadResponse temporaryPostLoad(Long userId) {
        List<TemporaryPost> postTemporaries = temporaryPostRepository.findByUserId(userId);
        return TemporaryPostLoadResponse.of(userId, postTemporaries);
    }
}
