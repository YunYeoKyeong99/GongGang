package com.yeokeong.gonggang.model.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReqPlaceCreate {
    @NotBlank
    private String name;
    @NotBlank
    private String location;
    @NotNull
    private String operatingTime;
    @NotNull
    private String menu;
    @NotNull
    private String linkUrl;
}
