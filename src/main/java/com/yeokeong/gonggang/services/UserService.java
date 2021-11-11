package com.yeokeong.gonggang.services;

import com.yeokeong.gonggang.common.Constant;
import com.yeokeong.gonggang.common.UserStatus;
import com.yeokeong.gonggang.httpException.ResponseError;
import com.yeokeong.gonggang.model.entity.User;
import com.yeokeong.gonggang.model.req.ReqUserSignIn;
import com.yeokeong.gonggang.model.req.ReqUserSignUp;
import com.yeokeong.gonggang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserSignService userSignService;
    private final UserRepository userRepository;

    public User signUp(ReqUserSignUp req) {

        String userName = req.getUserName();

        if (!userName.matches(Constant.USERNAME_REGEX)) {
            throw ResponseError.BadRequest.INVALID_ID.getResponseException();
        }

        User user = User.builder()
                .userName(userName)
                .pwd(passwordEncoder.encode(req.getPwd()))
                .nickName(req.getNickName())
                .status(UserStatus.NORMAL)
                .build();

        try {
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
            // TODO ID 중복
            // TODO NICKNAME 중복
        }

        return user;
    }

    public Authentication signIn(ReqUserSignIn req) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUserName(), req.getPwd())
        );

        if (auth == null || !auth.isAuthenticated()) {
            throw ResponseError.BadRequest.BAD_REQUEST.getResponseException();
        }

        return auth;
    }
}
