package com.carrotbay.common.handler;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.carrotbay.domain.history.HistoryService;
import com.carrotbay.domain.history.event.HistoryLoggingEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoryLoggingEventListener {

	private final HistoryService historyService;

	@Async //  메서드를 비동기적으로 실행할 수 있도록 Spring이 비동기 실행 환경을 활성화하는 설정
	//트랜잭션이 성공적으로 커밋된 이후에만 이벤트를 처리하도록 설정하는 어노테이션
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleEntityUpdated(HistoryLoggingEvent dto) {
		try {
			historyService.saveHistory(dto);
		} catch (Exception e) {
			log.error("handleEntityUpdated : 히스토리 저장 실패", e);
		}
	}
}
