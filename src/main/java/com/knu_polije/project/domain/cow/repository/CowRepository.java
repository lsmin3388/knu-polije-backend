package com.knu_polije.project.domain.cow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knu_polije.project.domain.cow.entity.Cow;
import com.knu_polije.project.domain.member.entity.Member;

public interface CowRepository extends JpaRepository<Cow, Long> {
	Optional<Cow> findByMemberAndCowNumber(Member member, Long cowNumber);
}
