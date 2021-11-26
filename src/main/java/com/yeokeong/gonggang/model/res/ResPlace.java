package com.yeokeong.gonggang.model.res;

import com.yeokeong.gonggang.config.AppConfig;
import com.yeokeong.gonggang.model.entity.Place;
import com.yeokeong.gonggang.model.entity.PlacePicture;
import com.yeokeong.gonggang.model.entity.Post;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ResPlace {

    private Long seq;
    private String name;
    private String location;
    private String operatingTime;
    private String menu;
    private String linkUrl;
    private List<String> pictureUrls;

    private ResPlace(Place place) {
        this.seq = place.getSeq();
        this.name = place.getName();
        this.location = place.getLocation();
        this.operatingTime = place.getOperatingTime();
        this.menu = place.getMenu();
        this.linkUrl = place.getLinkUrl();
        this.pictureUrls = place.getPictures().stream()
                .map(PlacePicture::getPath)
                .map(path -> AppConfig.imageUrl + path)
                .collect(Collectors.toList());

    }
    public static ResPlace of(Place place) {
        return new ResPlace(place);
    }
}
