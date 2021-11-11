package com.yeokeong.gonggang.model.res;

import com.yeokeong.gonggang.model.entity.User;
import lombok.Getter;

@Getter
public class ResUser {
    private Long seq;
    private String userName;
    private String nickName;

    private ResUser(User user) {
        this.seq = user.getSeq();
        this.userName = user.getUserName();
        this.nickName = user.getNickName();
    }

    public static ResUser of(User user) { return new ResUser(user); }
}
