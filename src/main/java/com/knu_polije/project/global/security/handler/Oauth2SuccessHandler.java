package com.knu_polije.project.global.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.knu_polije.project.global.jwt.dto.Token;
import com.knu_polije.project.global.jwt.provider.JwtTokenGenerator;
import com.knu_polije.project.global.security.details.PrincipalDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenGenerator jwtTokenGenerator;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 로그인 성공 1");
        PrincipalDetails oauth2User = (PrincipalDetails) authentication.getPrincipal();
        Token accessToken =  jwtTokenGenerator.generateAccessToken(authentication);

        response.addCookie(accessToken.cookie());
        // response.sendRedirect("http://localhost/board");
        response.sendRedirect("https://kpcowell.site");

        log.info("OAuth2 로그인에 성공하였습니다. 이메일 : {}",  oauth2User.getMember().getEmail());
        log.info("OAuth2 로그인에 성공하였습니다. Access Token : {}",  accessToken);
    }
}
