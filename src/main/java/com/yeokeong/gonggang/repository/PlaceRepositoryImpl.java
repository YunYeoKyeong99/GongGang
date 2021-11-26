package com.yeokeong.gonggang.repository;

import com.querydsl.core.types.Predicate;
import com.yeokeong.gonggang.model.entity.Place;
import com.yeokeong.gonggang.model.entity.QPlace;
import com.yeokeong.gonggang.model.entity.QPlacePicture;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;
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

        return from(qPlace)
                .select(qPlace)
                .leftJoin(qPlace.pictures, qPlacePicture).fetchJoin()
                .where(predicates)
                .orderBy(qPlace.seq.desc())
                .limit(pageSize)
                .fetch()
                .stream()
                .distinct()
                .collect(Collectors.toList());
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
