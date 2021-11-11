package com.yeokeong.gonggang.model;

import com.yeokeong.gonggang.common.Constant;
import com.yeokeong.gonggang.model.entity.User;
import lombok.Getter;

@Getter
public class UserAccount extends org.springframework.security.core.userdetails.User {
    private Long userSeq;

    public UserAccount(User user) {
        super(user.getUserName(), user.getPwd(), Constant.USER_AUTHORITIES);
        this.userSeq = user.getSeq();
    }
}
