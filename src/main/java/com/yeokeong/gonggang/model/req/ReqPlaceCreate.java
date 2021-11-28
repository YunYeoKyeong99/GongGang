package com.yeokeong.gonggang.model.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

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
    private List<ReqLink> links = new LinkedList<>();
}
