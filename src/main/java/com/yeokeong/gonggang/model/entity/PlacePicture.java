package com.yeokeong.gonggang.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Comparator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "place_picture")
public class PlacePicture extends AbstractTimeEntity implements Comparable<PlacePicture> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer turn;

    private String path;

    @ManyToOne
    @JoinColumn(name="place_seq")
    private Place place;

    @Override
    public int compareTo(PlacePicture placePicture) {
        // return this.turn > placePicture.turn ? -1 : 1;
        return Comparator
                .comparingInt(PlacePicture::getTurn)
//                내림차순
//                .reversed()
                .compare(this, placePicture);
    }
}
