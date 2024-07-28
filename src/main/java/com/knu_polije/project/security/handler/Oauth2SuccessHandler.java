package com.knu_polije.project.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.knu_polije.project.jwt.dto.Token;
import com.knu_polije.project.jwt.provider.JwtTokenGenerator;
import com.knu_polije.project.security.details.PrincipalDetails;

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
        PrincipalDetails oauth2User = (PrincipalDetails) authentication.getPrincipal();
        Token accessToken =  jwtTokenGenerator.generateAccessToken(authentication);

        response.addCookie(accessToken.cookie());
        response.sendRedirect("http://localhost:3000");

        log.info("OAuth2 로그인에 성공하였습니다. 이메일 : {}",  oauth2User.getMember().getEmail());
        log.info("OAuth2 로그인에 성공하였습니다. Access Token : {}",  accessToken);
    }
}
