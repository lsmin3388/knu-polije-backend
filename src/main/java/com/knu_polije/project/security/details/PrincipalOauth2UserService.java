package com.knu_polije.project.security.details;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.knu_polije.project.domain.member.entity.Member;
import com.knu_polije.project.domain.member.entity.Role;
import com.knu_polije.project.domain.member.service.MemberService;
import com.knu_polije.project.security.dto.Oauth2ResponseDto;
import com.knu_polije.project.security.dto.Oauth2ResponseMatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	private final MemberService memberService;
	private final Oauth2ResponseMatcher oauth2ResponseMatcher;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		Oauth2ResponseDto oauth2Response = oauth2ResponseMatcher.matcher(registrationId, oAuth2User);

		Member member = memberService.saveOrReturn(Member.builder()
			.email(oauth2Response.getEmail())
			.role(Role.ROLE_USER)
			.build());

		return new PrincipalDetails(member, oAuth2User.getAttributes());
	}
}
