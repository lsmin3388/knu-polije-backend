package com.knu_polije.project.domain.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knu_polije.project.domain.history.entity.History;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
