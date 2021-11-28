package com.yeokeong.gonggang.repository;

import com.querydsl.core.types.Predicate;
import com.yeokeong.gonggang.model.entity.Place;
import com.yeokeong.gonggang.model.entity.PlacePicture;
import com.yeokeong.gonggang.model.entity.QPlace;
import com.yeokeong.gonggang.model.entity.QPlacePicture;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlaceRepositoryImpl extends QuerydslRepositorySupport implements PlaceRepositorySupport {
    private final QPlace qPlace = QPlace.place;
    private final QPlacePicture qPlacePicture = QPlacePicture.placePicture;

    public PlaceRepositoryImpl() { super(Place.class); }


    @Override
    public List<Place> findAllByPlaceSeqLowerThan(
            Long userSeq,
            Integer pageSize,
            Long prevLastPlaceSeq
    ) {
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qPlace.seq::lt, prevLastPlaceSeq),
        };

        final List<Place> placeList = from(qPlace)
                .select(qPlace)
//                .leftJoin(qPlace.pictures, qPlacePicture).fetchJoin()
                .where(predicates)
                .orderBy(qPlace.seq.desc())
                .limit(pageSize)
                .fetch();

        placeList.forEach(p -> p.setPictures(new TreeSet<>()));

        // TODO OneToMany 모두 이 방식으로 변경
        // Place의 pircutres 조회
        from(qPlacePicture)
                .where(qPlacePicture.place.in(placeList))
                .fetch()
                .forEach(pic -> pic.getPlace().getPictures().add(pic));

        // pictures 정렬
//        placeList.forEach(p ->
//                p.getPictures().sort(Comparator.comparingInt(PlacePicture::getTurn)));

        return placeList;
    }

    @Override
    public Optional<Place> findPlaceInfoBySeq(Long seq, Long userSeq) {

        return from(qPlace)
                .select(qPlace)
                .leftJoin(qPlace.pictures, qPlacePicture).fetchJoin()
                .where(qPlace.seq.eq(seq))
                .fetch()
                .stream()
                .distinct()
                .findFirst();
    }

    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }
}
