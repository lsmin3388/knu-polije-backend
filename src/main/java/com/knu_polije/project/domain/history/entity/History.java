package com.knu_polije.project.domain.history.entity;

import com.knu_polije.project.domain.base.BaseTimeEntity;
import com.knu_polije.project.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History extends BaseTimeEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String inputFileName;

	private String outputFileName;

	@Column(length = 2048)
	private String data;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Member.class)
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder
	public History(String inputFileName, String outputFileName, String data, Member member) {
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.data = data;
		this.member = member;
	}
}
