package com.yeokeong.gonggang.repository;

import com.yeokeong.gonggang.model.entity.PostPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostPictureRepository extends JpaRepository<PostPicture, Long> {
    Optional<PostPicture> findFirstByPostSeqOrderByTurnDesc(Long postSeq);
}
