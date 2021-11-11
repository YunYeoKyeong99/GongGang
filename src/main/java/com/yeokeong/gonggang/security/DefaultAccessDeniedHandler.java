package com.yeokeong.gonggang.security;

import com.yeokeong.gonggang.httpException.ResponseError;
import com.yeokeong.gonggang.httpException.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException,
            ServletException {
        if (!response.isCommitted()) {
            final ResponseException e = ResponseError.Forbidden.NEED_NICK.getResponseException();
            response.sendError(
                    e.getHttpStatus().value(),
                    e.getMessage());
        }
    }
}