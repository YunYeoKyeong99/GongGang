package com.yeokeong.gonggang.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "place_picture")
public class PlacePicture extends AbstractTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer turn;

    private String path;

    @ManyToOne
    @JoinColumn(name="place_seq")
    private Place place;
}
