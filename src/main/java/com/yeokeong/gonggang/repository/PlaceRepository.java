package com.yeokeong.gonggang.repository;

import com.yeokeong.gonggang.model.entity.Place;
import com.yeokeong.gonggang.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceRepositorySupport{
}
