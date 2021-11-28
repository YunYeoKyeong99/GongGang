package com.yeokeong.gonggang.repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.yeokeong.gonggang.common.CategoryType;
import com.yeokeong.gonggang.common.CostType;
import com.yeokeong.gonggang.common.DurationTimeType;
import com.yeokeong.gonggang.common.TimingType;
import com.yeokeong.gonggang.model.entity.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PostRepositoryImpl extends QuerydslRepositorySupport implements PostRepositorySupport {
    private final QPost qPost = QPost.post;
    private final QUser qUser = QUser.user;
    private final QPostLike qPostLike = QPostLike.postLike;
    private final QPostPicture qPostPicture = QPostPicture.postPicture;
    private final QPostBookmark qPostBookmark = QPostBookmark.postBookmark;
    private final QPostCategory qPostCategory = QPostCategory.postCategory;

    public PostRepositoryImpl() {
        super(Post.class);
    }

    @Override
    public List<Post> findAllByUserSeq(
            Long userSeq,
            Integer pageSize,
            Long prevLastPostSeq
    ) {
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qPost.seq::lt, prevLastPostSeq)
        };

        return from(qPost)
                .select(new QPostProjection(qPost, qPostLike, qPostBookmark))
                //.innerJoin(qPost.user, qUser).fetchJoin()
//                .leftJoin(qPost.pictures, qPostPicture).fetchJoin()
                .leftJoin(qPostLike)
                .on(qPostLike.postSeq.eq(qPost.seq)
                        .and(qPostLike.userSeq.eq(userSeq)))
                .leftJoin(qPostBookmark)
                .on(qPostBookmark.postSeq.eq(qPost.seq)
                        .and(qPostBookmark.userSeq.eq(userSeq)))
                .where(predicates)
                .where(qPost.user.seq.eq(userSeq))
                .orderBy(qPost.seq.desc())
                .limit(pageSize)
                .fetch()
                .stream()
                .distinct()
                .map(PostProjection::getPost)
                .collect(Collectors.toList());


    }

    @Override
    public List<Post> findAllByPostBookMark(
            Long userSeq,
            Integer pageSize,
            Long prevLastPostSeq
    ) {
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qPost.seq::lt, prevLastPostSeq)
        };

        final List<Post> postList =  from(qPost)
                .select(new QPostProjection(qPost, qPostLike, qPostBookmark))
                .innerJoin(qPost.user, qUser).fetchJoin()
                .innerJoin(qPostBookmark)
                .on(qPostBookmark.postSeq.eq(qPost.seq)
                        .and(qPostBookmark.userSeq.eq(userSeq)))
//                .leftJoin(qPost.pictures, qPostPicture).fetchJoin()
                .leftJoin(qPostLike)
                .on(qPostLike.postSeq.eq(qPost.seq)
                        .and(qPostLike.userSeq.eq(userSeq)))
                .where(predicates)
                .orderBy(qPost.seq.desc())
                .limit(pageSize)
                .fetch()
                .stream()
                .distinct()
                .map(PostProjection::getPost)
                .collect(Collectors.toList());

        setPostPictures(postList);

        setPostCategories(postList);

        return postList;
    }

    @Override
    public List<Post> findAll(
            Long userSeq,
            TimingType timingType,
            CostType costType,
            DurationTimeType durationTimeType,
            CategoryType categoryType,
            Integer pageSize,
            Long prevLastPostSeq
    ) {
        // eq =
        // lt <
        // loe <=
        // gt >
        // goe >=
        // WHERE seq < 21
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qPost.seq::lt, prevLastPostSeq),
                predicateOptional(qPost.timingType::eq, timingType),
                predicateOptional(qPost.costType::eq, costType),
                predicateOptional(qPost.durationTimeType::eq, durationTimeType)
        };

        /**
          post 1 ~ 30

         // 첫 페이지 30 ~ 21
         SELECT *
         FROM post
         ORDER BY seq DESC
         LIMIT 10

         // req 21
         // 2 페이지 20 ~ 11
         SELECT *
         FROM post
         WHERE seq < 21
         ORDER BY seq DESC
         LIMIT 10

         // req 11
         // 3 페이지 10 ~ 1
         SELECT *
         FROM post
         WHERE seq < 11
         ORDER BY seq DESC
         LIMIT 10
         */

        List<Post> postList = from(qPost)
                .select(new QPostProjection(qPost, qPostLike, qPostBookmark))
                .innerJoin(qPost.user, qUser).fetchJoin()
//                .leftJoin(qPost.pictures, qPostPicture).fetchJoin()
                .leftJoin(qPostLike)
                .on(qPostLike.postSeq.eq(qPost.seq)
                        .and(qPostLike.userSeq.eq(userSeq)))
                .leftJoin(qPostBookmark)
                .on(qPostBookmark.postSeq.eq(qPost.seq)
                        .and(qPostBookmark.userSeq.eq(userSeq)))
                .where(predicates)
                .orderBy(qPost.seq.desc())
                .limit(pageSize)
                .fetch()
                .stream()
                .distinct()
                .map(PostProjection::getPost)
                .collect(Collectors.toList());

        setPostPictures(postList);

        setPostCategories(postList);

        if(categoryType != null) {
            return postList
                    .stream()
                    .filter( p -> p.getCategories().stream().anyMatch( pc -> pc.getId().getCategoryType() == categoryType) )
                    .collect(Collectors.toList());
        }

        return postList;
    }

    @Override
    public Optional<Post> findPostInfoBySeq(Long seq, Long userSeq) {

        return from(qPost)
                .select(new QPostProjection(qPost, qPostLike, qPostBookmark))
                .innerJoin(qPost.user, qUser).fetchJoin()
//                .leftJoin(qPost.pictures, qPostPicture).fetchJoin()
                .leftJoin(qPostLike)
                    .on(qPostLike.postSeq.eq(qPost.seq)
                        .and(qPostLike.userSeq.eq(userSeq)))
                .leftJoin(qPostBookmark)
                    .on(qPostBookmark.postSeq.eq(qPost.seq)
                        .and(qPostBookmark.userSeq.eq(userSeq)))
                .where(qPost.seq.eq(seq))
                .fetch()
                .stream()
                .distinct()
                .map(PostProjection::getPost)
                .findFirst();
    }

    private void setPostPictures(List<Post> postList) {
        postList.forEach(p -> p.setPictures(new TreeSet<>()));

        // TODO OneToMany 모두 이 방식으로 변경
        from(qPostPicture)
                .where(qPostPicture.post.in(postList))
                .fetch()
                .forEach(pic -> pic.getPost().getPictures().add(pic));
    }

    private void setPostCategories(List<Post> postList) {
        postList.forEach(p -> p.setCategories(new LinkedList<>()));

        from(qPostCategory)
                .where(qPostCategory.id.post.in(postList))
                .fetch()
                .forEach(category -> category.getId().getPost().getCategories().add(category));
    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }
}
