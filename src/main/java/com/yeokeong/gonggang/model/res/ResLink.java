package com.yeokeong.gonggang.model.res;

import com.yeokeong.gonggang.model.Link;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResLink {
    private String name;
    private String url;

    private ResLink(Link link){
        this.name = link.getName();
        this.url = link.getUrl();
    }

    public static ResLink of(Link link) {
        return new ResLink(link);
    }
}
