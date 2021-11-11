package com.yeokeong.gonggang.model.entity;

import lombok.*;

import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUserSeq implements Serializable {
    private Long postSeq;
    private Long userSeq;
}
