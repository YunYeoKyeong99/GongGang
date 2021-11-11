package com.yeokeong.gonggang.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post_bookmark")
@IdClass(PostUserSeq.class)
public class PostBookmark extends AbstractTimeEntity {

    @Id
    private Long postSeq;

    @Id
    private Long userSeq;
}
