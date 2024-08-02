package com.knu_polije.project.domain.cow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knu_polije.project.domain.cow.entity.Cow;

public interface CowRepository extends JpaRepository<Cow, Long> {
}
