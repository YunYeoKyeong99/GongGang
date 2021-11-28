package com.yeokeong.gonggang.model.entity;

import com.yeokeong.gonggang.common.CategoryType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"categoryType", "post"})
public class PostCategoryId implements Serializable {
    @Enumerated(EnumType.STRING)
    @Column(name = "category_name")
    private CategoryType categoryType;

    @ManyToOne
    @JoinColumn(name = "post_seq")
    private Post post;
}
