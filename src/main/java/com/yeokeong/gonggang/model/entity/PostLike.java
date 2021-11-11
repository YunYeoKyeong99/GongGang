package com.yeokeong.gonggang.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post_like")
@IdClass(PostUserSeq.class)
public class PostLike extends AbstractTimeEntity {

    @Id
    private Long postSeq;

    @Id
    private Long userSeq;
}
