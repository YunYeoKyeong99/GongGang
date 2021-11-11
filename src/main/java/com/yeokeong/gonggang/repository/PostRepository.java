package com.yeokeong.gonggang.repository;

import com.yeokeong.gonggang.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositorySupport {

    @Transactional
    @Modifying
    @Query("UPDATE post SET likeCnt = likeCnt + 1 WHERE seq = :seq")
    int incrementLikeCnt(@Param("seq") Long seq);

    @Transactional
    @Modifying
    @Query("UPDATE post SET likeCnt = likeCnt - 1 WHERE seq = :seq")
    int decrementLikeCnt(@Param("seq") Long seq);
}
