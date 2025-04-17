package com.carrotbay.domain.history.dto;

import com.carrotbay.domain.history.History;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HistoryRequestDto {

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CreateHistoryDto {
		private String tableName;
		private String operation;
		private String beforeValue;
		private String afterValue;
		private Long entityId;
		private Long createBy;

		public History toEntity() {
			return History.builder()
				.tableName(this.tableName)
				.operation(this.operation)
				.beforeValue(this.beforeValue)
				.afterValue(this.afterValue)
				.entityId(this.entityId)
				.createdBy(this.createBy)
				.build();
		}
	}
}
