package com.yeokeong.gonggang.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeokeong.gonggang.common.Constant;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNullApi;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class ApiLogFilter extends OncePerRequestFilter {
    //private final static Pattern reqParamPwPattern = Pattern.compile("(?<=\\\"pw\\\":\\\")[\\S]+(?=\\\"\\,)");

    private final String activeProfiles;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        MDC.clear();

        final LocalDateTime startDatetime = LocalDateTime.now();
        ApiLog.ApiLogBuilder apiLogBuilder = ApiLog.builder()
                .profiles(activeProfiles)
                .time(startDatetime);

        final String threadId = UUID.randomUUID().toString();
        MDC.put(Constant.MDC_KEY_THREAD_ID, threadId);
        apiLogBuilder.threadId(threadId);

        // TODO 로그인 구현 후 손보기
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if(auth != null && auth.isAuthenticated()) {
//            MDC.put(Constant.MDC_KEY_USER_SEQ, ((User)auth.getPrincipal()).getSeq().toString());
//        }

        ContentCachingRequestWrapper wrappedRequest = request instanceof ContentCachingRequestWrapper
                ? (ContentCachingRequestWrapper) request
                : new ContentCachingRequestWrapper(request);


        // TODO url을 /api부터 시작하는 것에 대해 논의해보기 (/api가 아닌 것과 구분이 가능하다.)
        boolean cantWrap = !request.getRequestURI().startsWith("/api");

        ContentCachingResponseWrapper wrappedResponse = response instanceof ContentCachingResponseWrapper
                ? (ContentCachingResponseWrapper) response
                : new ContentCachingResponseWrapper(response);

        try {
            if (cantWrap)
                filterChain.doFilter(request, response);
            else
                filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            final LocalDateTime endDatetime = LocalDateTime.now();
            final long durationMillis = ChronoUnit.MILLIS.between(startDatetime, endDatetime);
            apiLogBuilder.duration(durationMillis);

            try {
                wrappedResponse.copyBodyToResponse();
            } catch (IOException ignored) {
            }

            setRequestLog(wrappedRequest, apiLogBuilder);
            setResponseLog(wrappedResponse, apiLogBuilder);

            try {
                log.info(objectMapper.writeValueAsString(apiLogBuilder.build()));
            } catch (Exception e) {
            }
        }
    }

    private void setRequestLog(ContentCachingRequestWrapper wrappedRequest, ApiLog.ApiLogBuilder apiLogBuilder) {
        String userIp = wrappedRequest.getHeader("X-Forwarded-For");
        if (userIp == null)
            userIp = wrappedRequest.getRemoteAddr();

        apiLogBuilder
                .userIp(userIp)
                .userAgent(wrappedRequest.getHeader(HttpHeaders.USER_AGENT))
                .method(wrappedRequest.getMethod())
                .path(wrappedRequest.getRequestURI())
                .patternPath((String) wrappedRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE));

        HttpMethod method = HttpMethod.valueOf(wrappedRequest.getMethod());

        if (method == HttpMethod.GET) {
            apiLogBuilder.reqParam(wrappedRequest.getQueryString());
        } else if (method == HttpMethod.POST || method == HttpMethod.PUT) {
            String reqParam = getMessagePayload(wrappedRequest);
//            reqParam = hidePw(reqParam);
            apiLogBuilder.reqParam(reqParam);
        }
    }

    private void setResponseLog(ContentCachingResponseWrapper wrappedResponse, ApiLog.ApiLogBuilder apiLogBuilder) {

        apiLogBuilder.statusCode(wrappedResponse.getStatus());

        if (HttpStatus.valueOf(wrappedResponse.getStatus()).isError()) {
            apiLogBuilder.resBody(getMessagePayload(wrappedResponse));
        }

        apiLogBuilder.userSeq(
                Optional.ofNullable(MDC.get(Constant.MDC_KEY_USER_SEQ))
                        .filter(v -> !StringUtils.hasLength(v))
                        .map(Integer::parseInt)
                        .orElse(null));
    }

    private String getMessagePayload(ContentCachingRequestWrapper wrappedRequest) {
        byte[] buf = wrappedRequest.getContentAsByteArray();
        if (buf.length <= 0)
            return null;
        try {
            return tripJsonString(
                    new String(buf, 0, Math.min(buf.length, 1000), wrappedRequest.getCharacterEncoding())
            );
        } catch (UnsupportedEncodingException ex) {
            return "[unknown]";
        }
    }

    private String getMessagePayload(ContentCachingResponseWrapper wrappedResponse) {
        byte[] buf = wrappedResponse.getContentAsByteArray();
        if (buf.length <= 0)
            return null;
        try {
            return new String(buf, 0, Math.min(buf.length, 1000), wrappedResponse.getCharacterEncoding());
        } catch (UnsupportedEncodingException ex) {
            return "[unknown]";
        }
    }

    private String tripJsonString(String jsonString) {
        StringBuilder sb = new StringBuilder(jsonString);

        boolean prevBackslash = false;
        boolean openDoubleQuote = false;
        for (int i = 0; i < sb.length(); i++) {
            char ch = sb.charAt(i);
            if (openDoubleQuote) {
                if (!prevBackslash && ch == '"')
                    openDoubleQuote = false;
            } else if (ch == '"') {
                openDoubleQuote = true;
            } else if (ch == ' ' || ch == '\n' || ch == '\t' || ch == '\r') {
                sb.deleteCharAt(i--);
            }

            prevBackslash = ch == '\\';
        }

        return sb.toString();
    }

//    private String hidePw(String reqParam) {
//        // TODO 로그인 짤 때 변수명에 맞게 수정
//        if (!StringUtils.hasLength(reqParam) || !reqParam.contains("\"pw\"")) {
//            return reqParam;
//        }
//        Matcher matcher = reqParamPwPattern.matcher(reqParam);
//        if (!matcher.find()) {
//            return reqParam;
//        }
//        String pw = matcher.group();
//        StringBuilder starPw = new StringBuilder();
//        for (int i = 0; i < pw.length(); i++) {
//            starPw.append('*');
//        }
//        return reqParam.substring(0, matcher.start()) + starPw + reqParam.substring(matcher.end());
//    }

    @Builder
    @Getter
    static class ApiLog {
        private final String profiles;
        @Builder.Default
        private final String logType = "API";
        private final LocalDateTime time;
        private final String threadId;
        private final Long duration;
        private final String userIp;
        private final String userAgent;
        private final Integer userSeq;
        private final String method;
        private final String path;
        private final String patternPath;
        private final String reqParam;
        private final Integer statusCode;
        private final String resBody;
    }
}