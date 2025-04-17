package com.carrotbay.common.handler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.carrotbay.domain.history.HistoryService;
import com.carrotbay.domain.history.dto.HistoryRequestDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntityUpdateEventListener {

	private final HistoryService historyService;

	@EventListener // ApplicationEventPublisher 를 통해 이벤트가 발행되면 해당 메서드가 실행됨
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // 트랜잭션이 성공적으로 커밋된 이후에 이벤트 처리
	public void handleEntityUpdated(HistoryRequestDto.CreateHistoryDto dto) {
		historyService.saveHistory(dto);
	}
}
