package com.knu_polije.project.domain.history.entity;

import com.knu_polije.project.domain.base.BaseTimeEntity;
import com.knu_polije.project.domain.cow.entity.Cow;
import com.knu_polije.project.domain.member.entity.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History extends BaseTimeEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cow_id")
	private Cow cow;

	@Enumerated(EnumType.STRING)
	private DetectType detectType;

	private String inputImgName;
	private String outputImgName;
	private String outputData;

	@Builder
	public History(Member member, Cow cow, DetectType detectType, String inputImgName, String outputImgName,
		String outputData) {
		this.member = member;
		this.cow = cow;
		this.detectType = detectType;
		this.inputImgName = inputImgName;
		this.outputImgName = outputImgName;
		this.outputData = outputData;
	}
}
