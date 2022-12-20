package com.zzangmin.gesipan.component.basiccrud.controller;

import com.zzangmin.gesipan.component.basiccrud.service.TemporaryPostService;
import com.zzangmin.gesipan.component.login.argumentresolver.Auth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TemporaryPostController {

    private final TemporaryPostService temporaryPostService;

    @DeleteMapping("/temporary/post/{temporaryPostId}")
    public ResponseEntity<String> removeTemporaryPost(@Auth Long userId, @PathVariable Long temporaryPostId) {
        temporaryPostService.postTemporaryDelete(userId, temporaryPostId);
        return ResponseEntity.ok("remove temporary post success");
    }
}
