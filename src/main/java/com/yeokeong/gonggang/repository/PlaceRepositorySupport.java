package com.yeokeong.gonggang.repository;

import com.yeokeong.gonggang.model.entity.Place;

import java.util.List;
import java.util.Optional;

public interface PlaceRepositorySupport {

    List<Place> findAllByPlaceSeqLowerThan(
            Long userSeq,
            Integer pageSize,
            Long prevLastPlaceSeq
    );
    Optional<Place> findPlaceInfoBySeq(Long seq, Long userSeq);
}
