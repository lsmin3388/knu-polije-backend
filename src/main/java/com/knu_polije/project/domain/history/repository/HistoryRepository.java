package com.knu_polije.project.domain.history.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.knu_polije.project.domain.history.entity.History;
import com.knu_polije.project.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;

public interface HistoryRepository extends JpaRepository<History, Long> {
	List<History> findAllByMember(Member member);
}
