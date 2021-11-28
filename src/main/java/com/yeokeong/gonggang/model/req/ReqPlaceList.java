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

    // select in 이 30개를 넘어가면 select 30개와 다를바가 없다. (range scan -> full scan 됨)
    @Min(1L) @Max(30L)
    private Integer pageSize = 20;
}