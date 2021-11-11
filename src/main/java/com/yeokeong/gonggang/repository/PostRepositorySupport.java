package com.yeokeong.gonggang.repository;

import com.yeokeong.gonggang.common.CostType;
import com.yeokeong.gonggang.common.DurationTimeType;
import com.yeokeong.gonggang.common.TimingType;
import com.yeokeong.gonggang.model.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepositorySupport {
    List<Post> findAllByPostSeqLowerThan(
            Long userSeq,
            TimingType timingType,
            CostType costType,
            DurationTimeType durationTimeType,
            Integer pageSize,
            Long prevLastPostSeq
    );
    Optional<Post> findPostInfoBySeq(Long seq, Long userSeq);
}
