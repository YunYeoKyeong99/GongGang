package com.yeokeong.gonggang.repository;

import com.yeokeong.gonggang.model.entity.PostLike;
import com.yeokeong.gonggang.model.entity.PostUserSeq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostUserSeq> {
    Integer countByPostSeqAndUserSeq(Long postSeq, Long userSeq);
}
