package com.carrotbay.domain.history.event;

import com.carrotbay.domain.history.History;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryLoggingEvent {
	private String tableName;
	private String operation;
	private String beforeValue;
	private String afterValue;
	private Long entityId;

	public History toEntity() {
		return History.builder()
			.tableName(this.tableName)
			.operation(this.operation)
			.beforeValue(this.beforeValue)
			.afterValue(this.afterValue)
			.entityId(this.entityId)
			.build();
	}
}
