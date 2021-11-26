package com.yeokeong.gonggang.model.entity;

import com.yeokeong.gonggang.common.CategoryType;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

public class PostCategoryId implements Serializable {
    @Enumerated(EnumType.STRING)
    @Column(name = "category_name")
    private CategoryType categoryType;

    @Column(name = "post_seq")
    private Long postSeq;
}
