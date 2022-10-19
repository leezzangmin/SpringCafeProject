package com.zzangmin.gesipan.layer.basiccrud.entity;

public enum Categories {
    //휴지통, 자유, 질문;
    TRASH, FREE, QNA;

    public static Long castCategoryNameToCategoryId(String categoryName) {
        try {
            return Long.valueOf(Categories.valueOf(categoryName).ordinal());
        } catch(Exception e) {
            throw new IllegalArgumentException("해당하는 카테고리가 없습니다. 잘못된 입력");
        }
    }
}
