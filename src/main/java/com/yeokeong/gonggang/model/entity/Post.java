package com.yeokeong.gonggang.model.entity;

import com.yeokeong.gonggang.common.*;
import com.yeokeong.gonggang.model.Link;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post")
@EqualsAndHashCode(callSuper = false, of = {"seq"})
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

    @Convert(converter = LinkConverter.class)
    private List<Link> links;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @OneToMany(mappedBy = "post")
    private Set<PostPicture> pictures;

    @OneToMany
    @JoinColumn(name = "post_seq")
    private List<PostCategory> categories;

    @Transient
    private Boolean isLike;

    @Transient
    private Boolean isBookmark;
}
