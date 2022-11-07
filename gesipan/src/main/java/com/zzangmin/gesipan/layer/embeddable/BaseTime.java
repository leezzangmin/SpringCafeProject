package com.zzangmin.gesipan.layer.embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BaseTime {

    @Column(nullable = false, columnDefinition = "DATETIME", updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime updatedAt;

    public void refreshUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
