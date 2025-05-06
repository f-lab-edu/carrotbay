package com.carrotbay.domain.history;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "history")
@Getter
@Entity
public class History {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "table_name", nullable = false)
	private String tableName;

	@Column(name = "operation", nullable = false)
	private String operation;

	@Column(name = "before_value", nullable = false, columnDefinition = "TEXT")
	private String beforeValue;

	@Column(name = "after_value", nullable = false, columnDefinition = "TEXT")
	private String afterValue;

	@Column(name = "entity_id", nullable = false)
	private Long entityId;

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Builder
	public History(String tableName, String operation, String beforeValue, String afterValue, Long entityId) {
		this.tableName = tableName;
		this.operation = operation;
		this.beforeValue = beforeValue;
		this.afterValue = afterValue;
		this.entityId = entityId;
	}
}
