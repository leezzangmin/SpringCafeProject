package com.zzangmin.gesipan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class PostCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postCategoryId;
    @Enumerated(EnumType.STRING)
    private Categories categoryName;

}
