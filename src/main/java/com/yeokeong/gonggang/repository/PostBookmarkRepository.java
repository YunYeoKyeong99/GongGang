package com.yeokeong.gonggang.repository;

import com.yeokeong.gonggang.model.entity.PostBookmark;
import com.yeokeong.gonggang.model.entity.PostUserSeq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostBookmarkRepository extends JpaRepository<PostBookmark, PostUserSeq> {
    Integer countByPostSeqAndUserSeq(Long postSeq, Long userSeq);
}
