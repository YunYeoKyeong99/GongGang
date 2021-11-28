package com.yeokeong.gonggang.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Comparator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post_picture")
public class PostPicture extends AbstractTimeEntity implements Comparable<PostPicture> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer turn;

    private String path;

    @ManyToOne
    @JoinColumn(name="post_seq")
    private Post post;

    @Override
    public int compareTo(PostPicture postPicture) {
        return Comparator
                .comparingInt(PostPicture::getTurn)
                .compare(this, postPicture);
    }
}
