package com.knu_polije.project.security.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.knu_polije.project.domain.member.service.MemberService;
import com.knu_polije.project.jwt.provider.JwtTokenValidator;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends GenericFilterBean {
    private final MemberService memberService;
    private final JwtTokenValidator jwtTokenValidator;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;

        if (((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
            chain.doFilter(request, response);
        }

        String token = jwtTokenValidator.resolveToken(servletRequest);
        if (token != null && jwtTokenValidator.validateToken(token)) {
            Authentication authentication
                    = jwtTokenValidator.getAuthentication(token, memberService);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
