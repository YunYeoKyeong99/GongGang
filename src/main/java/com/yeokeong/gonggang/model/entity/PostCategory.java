package com.yeokeong.gonggang.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post_category")
public class PostCategory extends AbstractTimeEntity {

    @EmbeddedId
    private PostCategoryId id;
}
