package com.yeokeong.gonggang.security;

import com.yeokeong.gonggang.model.UserAccount;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SessionUserArgResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.getParameterAnnotation(SessionUser.class) == null) {
            return false;
        }

        // 로그인을 했을때만 동작하도록
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLogined = auth != null && auth.isAuthenticated();

        return isLogined;
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return ((UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserSeq();
    }
}