package com.zzangmin.gesipan.layer.basiccrud.service;

import com.zzangmin.gesipan.layer.basiccrud.repository.TemporaryPostRepository;
import com.zzangmin.gesipan.layer.login.repository.UsersRepository;
import com.zzangmin.gesipan.layer.basiccrud.dto.temporarypost.TemporaryPostLoadResponse;
import com.zzangmin.gesipan.layer.basiccrud.dto.temporarypost.TemporaryPostSaveRequest;
import com.zzangmin.gesipan.layer.basiccrud.entity.TemporaryPost;
import com.zzangmin.gesipan.layer.login.entity.Users;
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

    @Transactional
    void postTemporaryDelete(Long userId, Long temporaryPostId) {
        if (temporaryPostId != null && temporaryPostRepository.findByUserId(userId)
                .stream()
                .anyMatch(i -> i.getTempPostId().equals(temporaryPostId))) {
            temporaryPostRepository.deleteById(temporaryPostId);
        }
    }

    @Transactional(readOnly = true)
    public TemporaryPostLoadResponse temporaryPostLoad(Long userId) {
        List<TemporaryPost> postTemporaries = temporaryPostRepository.findByUserId(userId);
        return TemporaryPostLoadResponse.of(userId, postTemporaries);
    }
}
