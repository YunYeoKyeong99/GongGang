package com.yeokeong.gonggang.model.entity;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class PostProjection implements Comparable<PostProjection> {
    private Post post;
    private PostLike myLike;
    private PostBookmark myBookmark;

    @QueryProjection
    public PostProjection(Post post, PostLike myLike, PostBookmark myBookmark) {
        this.post = post;
        this.myLike = myLike;
        this.myBookmark = myBookmark;
    }

    public Post getPost() {
        if(post != null) {
            post.setIsLike(myLike != null);
            post.setIsBookmark(myBookmark != null);
        }

        return post;
    }

    @Override
    public int compareTo(PostProjection p) {
        return post.getSeq().compareTo(p.getPost().getSeq());
    }
}
