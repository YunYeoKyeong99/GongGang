package com.yeokeong.gonggang.model.res;

import com.yeokeong.gonggang.common.CategoryType;
import com.yeokeong.gonggang.common.CostType;
import com.yeokeong.gonggang.common.DurationTimeType;
import com.yeokeong.gonggang.common.TimingType;
import com.yeokeong.gonggang.config.AppConfig;
import com.yeokeong.gonggang.model.Link;
import com.yeokeong.gonggang.model.entity.Post;
import com.yeokeong.gonggang.model.entity.PostCategory;
import com.yeokeong.gonggang.model.entity.PostPicture;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class ResPost {
    private Long seq;
    private String title;
    private String content;
    private TimingType timingType;
    private DurationTimeType durationTimeType;
    private CostType costType;
    private List<CategoryType> categoryTypes;
    private ResUser user;
    private List<String> pictureUrls;
    private Long likeCnt;
    private Boolean isLike;
    private Boolean isBookmark;
    private LocalDateTime createdAt;
    private List<ResLink> links;

    private ResPost(Post post) {
        this.seq = post.getSeq();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.timingType = post.getTimingType();
        this.durationTimeType = post.getDurationTimeType();
        this.costType = post.getCostType();
        this.categoryTypes = post.getCategories().stream()
                .map(item -> item.getId().getCategoryType())
                .collect(Collectors.toList());
        this.user = ResUser.of(post.getUser());
        this.pictureUrls = post.getPictures().stream()
                .map(PostPicture::getPath)
                .map(path -> AppConfig.imageUrl + path)
                .collect(Collectors.toList());
        this.likeCnt = post.getLikeCnt();
        this.isLike = post.getIsLike();
        this.isBookmark = post.getIsBookmark();
        this.createdAt = post.getCreatedAt();
        this.links = Optional.ofNullable(post.getLinks())
                .stream()
                .flatMap(Collection::stream)
                .map(ResLink::of)
                .collect(Collectors.toList());
    }

    public static ResPost of(Post post) {
        return new ResPost(post);
    }
}
