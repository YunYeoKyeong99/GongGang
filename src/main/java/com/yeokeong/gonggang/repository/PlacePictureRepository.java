package com.yeokeong.gonggang.repository;

import com.yeokeong.gonggang.model.entity.PlacePicture;
import com.yeokeong.gonggang.model.entity.PostPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlacePictureRepository extends JpaRepository<PlacePicture, Long> {
    Optional<PlacePicture> findFirstByPlaceSeqOrderByTurnDesc(Long placeSeq);
}
