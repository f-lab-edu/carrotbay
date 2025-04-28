package com.carrotbay.domain.history;

import org.springframework.stereotype.Service;

import com.carrotbay.domain.history.event.HistoryLoggingEvent;
import com.carrotbay.domain.history.repository.HistoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HistoryService {
	private final HistoryRepository historyRepository;

	@Transactional
	public void saveHistory(HistoryLoggingEvent dto) {
		historyRepository.save(dto.toEntity());
	}
}
