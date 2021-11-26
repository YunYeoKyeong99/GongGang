package com.yeokeong.gonggang.model.entity;

import com.yeokeong.gonggang.common.*;
import jdk.jfr.Category;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post")
public class Post extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private TimingType timingType;

    @Enumerated(EnumType.STRING)
    private DurationTimeType durationTimeType;

    @Enumerated(EnumType.STRING)
    private CostType costType;

    private Long likeCnt;

    private String linkUrl;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<PostPicture> pictures;

    @OneToMany
    @JoinColumn(name = "post_seq")
    private List<PostCategory> categories;

    @Transient
    private Boolean isLike;

    @Transient
    private Boolean isBookmark;
}
