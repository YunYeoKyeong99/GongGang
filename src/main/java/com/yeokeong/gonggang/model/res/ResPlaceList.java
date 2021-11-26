package com.yeokeong.gonggang.model.res;

import com.yeokeong.gonggang.config.AppConfig;
import com.yeokeong.gonggang.model.entity.Place;
import com.yeokeong.gonggang.model.entity.PlacePicture;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResPlaceList {
    private Long seq;
    private String name;
    private List<String> pictureUrls;

    private ResPlaceList(Place place){
        this.seq = place.getSeq();
        this.name = place.getName();
        this.pictureUrls = place.getPictures().stream()
                .map(PlacePicture::getPath)
                .map(path -> AppConfig.imageUrl + path)
                .collect(Collectors.toList());
    }

    public static ResPlaceList of(Place place) { return new ResPlaceList(place); }


}
