package com.carrotbay.domain.auction;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.carrotbay.domain.auction.dto.AuctionDto;
import com.carrotbay.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "auction")
@Getter
@Entity
public class Auction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", nullable = false, length = 20)
	private String title;

	@Column(columnDefinition = "TEXT", name = "content", nullable = false)
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 10)
	private AuctionStatus status;

	@Column(name = "end_date", nullable = false)
	private LocalDateTime endDate;

	@Column(name = "actual_end_date", nullable = false)
	private LocalDateTime actualEndDate;

	@Column(name = "minimum_price", nullable = false)
	private int minimumPrice;

	@Column(name = "instant_price", nullable = false)
	private int instantPrice;

	@Column(name = "is_delete", nullable = false)
	private boolean isDelete;

	@LastModifiedDate
	@Column(name = "modified_at", nullable = false)
	private LocalDateTime modifiedAt;

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private User createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by", nullable = false)
	private User updateBy;

	@ManyToOne(fetch = FetchType.LAZY) // 여러 개의 엔티티가 하나의 엔티티를 참조하는 관계를 나타낸다.
	@JoinColumn(name = "successful_bidder_user_id") // 외래 키 컬럼을 지정하는 어노테이션. @ManyToOne 관계에서 연결할 외래 키 컬럼 이름을 설정함.
	private User successfulBidder;

	@Builder
	public Auction(Long id, String title, String content, AuctionStatus status, LocalDateTime endDate,
		LocalDateTime actualEndDate, int minimumPrice, int instantPrice, boolean isDelete, LocalDateTime modifiedAt,
		LocalDateTime createdAt, User createdBy, User updateBy, User successfulBidder) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.status = status;
		this.endDate = endDate;
		this.actualEndDate = actualEndDate;
		this.minimumPrice = minimumPrice;
		this.instantPrice = instantPrice;
		this.isDelete = isDelete;
		this.modifiedAt = modifiedAt;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updateBy = updateBy;
		this.successfulBidder = successfulBidder;
	}

	public void update(AuctionDto.UpsertAuctionDto dto) {
		this.title = dto.getTitle();
		this.content = dto.getContent();
		this.endDate = dto.getEndDate();
		this.minimumPrice = dto.getMinimumPrice();
		this.instantPrice = dto.getInstantPrice();
	}

	public void update(boolean isDelete) {
		this.isDelete = isDelete;
		this.status = AuctionStatus.CANCEL;
	}
}
