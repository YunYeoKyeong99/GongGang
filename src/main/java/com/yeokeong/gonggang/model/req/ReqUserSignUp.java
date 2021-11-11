package com.yeokeong.gonggang.model.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ReqUserSignUp {
    @NotBlank
    private String userName;
    @NotBlank
    private String pwd;
    @NotBlank
    private String nickName;
}