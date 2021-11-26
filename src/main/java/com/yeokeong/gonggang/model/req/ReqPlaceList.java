package com.yeokeong.gonggang.model.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
public class ReqPlaceList {

    @Min(1L)
    private Long prevLastPostSeq;

    @Min(1L) @Max(50L)
    private Integer pageSize = 20;
}