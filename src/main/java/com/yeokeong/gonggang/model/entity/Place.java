package com.yeokeong.gonggang.model.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "place")
public class Place extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String name;

    private String location;

    private String operatingTime;

    private String menu;

    private String linkUrl;

    @OneToMany(mappedBy = "place")
    private List<PlacePicture> pictures;

}
