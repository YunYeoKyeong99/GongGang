package com.yeokeong.gonggang.model.entity;


import com.yeokeong.gonggang.common.CategoryType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post_category")
@IdClass(PostCategoryId.class)
public class PostCategory extends AbstractTimeEntity {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "category_name")
    private CategoryType categoryType;

    @Id
    @Column(name = "post_seq")
    private Long postSeq;
}
