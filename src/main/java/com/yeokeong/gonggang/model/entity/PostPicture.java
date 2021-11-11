package com.yeokeong.gonggang.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post_picture")
public class PostPicture extends AbstractTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer turn;

    private String path;

    @ManyToOne
    @JoinColumn(name="post_seq")
    private Post post;
}
