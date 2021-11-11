package com.yeokeong.gonggang.model.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReqUserSignIn {
    @NotBlank
    private String userName;
    @NotBlank
    private String pwd;
}