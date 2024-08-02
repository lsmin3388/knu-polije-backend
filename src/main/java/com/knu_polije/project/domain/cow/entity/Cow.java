package com.knu_polije.project.domain.cow.entity;

import com.knu_polije.project.domain.member.entity.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Cow {
	@Id @GeneratedValue
	private Long id;

	private Long cowNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	private String cowBreed;
	private String cowWeight;
}
