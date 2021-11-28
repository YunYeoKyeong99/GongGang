package com.yeokeong.gonggang.model.req;

import com.yeokeong.gonggang.model.Link;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class ReqLink {
    @NotBlank
    private String name;
    @NotBlank
    //@Pattern(regexp = "(http|https)://(\\w+:{0,1}\\w*@)?(\\S+)(:[0-9]+)?(/|/([\\w#!:.?+=&%@!\\-/]))?")
    private String url;

    public Link toLink() {
        return Link.builder()
                .name(name)
                .url(url)
                .build();
    }
}
