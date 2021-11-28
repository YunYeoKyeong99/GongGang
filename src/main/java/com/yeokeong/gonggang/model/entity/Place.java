package com.yeokeong.gonggang.model.entity;


import com.yeokeong.gonggang.common.LinkConverter;
import com.yeokeong.gonggang.model.Link;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

    @Convert(converter = LinkConverter.class)
    private List<Link> links;

    @OneToMany(mappedBy = "place")
    private Set<PlacePicture> pictures;

}
