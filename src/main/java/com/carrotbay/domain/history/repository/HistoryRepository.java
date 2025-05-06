package com.carrotbay.domain.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carrotbay.domain.history.History;

public interface HistoryRepository extends JpaRepository<History, Long> {

}
