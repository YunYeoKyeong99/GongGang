package com.yeokeong.gonggang.controller;

import com.yeokeong.gonggang.common.Constant;
import com.yeokeong.gonggang.model.UserAccount;
import com.yeokeong.gonggang.model.entity.User;
import com.yeokeong.gonggang.model.req.ReqUserSignIn;
import com.yeokeong.gonggang.model.req.ReqUserSignUp;
import com.yeokeong.gonggang.services.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //
//    @SwaggerResponseError({
//            ResponseError.INVALID_ID,
//            ResponseError.EXISTS_ID,
//            ResponseError.UNEXPECTED_ERROR
//    })
    @ApiOperation("가입 (ID)")
    @PostMapping(value = "/v1/users/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> signUpUser(
            @RequestBody @Valid ReqUserSignUp reqUserSignUp
    ) {
        userService.signUp(reqUserSignUp);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @SwaggerResponseError({
//            ResponseError.BAD_REQUEST
//    })
    @ApiOperation("로그인 (ID, 소셜)")
    @PostMapping(value = "/v1/users/signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> signInUser(
            @RequestBody @Valid ReqUserSignIn reqUserSignIn,
            HttpSession session
    ) {

        // TODO Param Validation

        // 로그인
        Authentication auth = userService.signIn(reqUserSignIn);

        // 앱 API를 만들때 사용 (로그인이 되어있는지 인지), 세션로그인
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @SwaggerResponseError({})
    @ApiOperation("로그아웃")
    @PostMapping("/v1/users/signout")
    public ResponseEntity<Void> signOut() {
        throw new IllegalStateException("This Method not working");
    }
}
