package com.carrotbay.domain.bid;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.carrotbay.domain.auction.Auction;
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
@Table(name = "bid")
@Getter
@Entity
public class Bid {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "bid_price", nullable = false)
	private int bidPrice;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private BidStatus status;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY) // JPA에서 두 엔티티 간 1:1 관계를 설정하면서, 관련 엔티티를 필요할 때만 불러오도록 지시하는 어노테이션
	@JoinColumn(name = "auction_id")
	private Auction auction;

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "created_by", nullable = false)
	private Long createdBy;

	@LastModifiedDate
	@Column(name = "modified_at", nullable = true)
	private LocalDateTime modifiedAt;

	@Builder
	public Bid(Long id, int bidPrice, BidStatus status, boolean isDeleted, LocalDateTime createdAt,
		User user, Long createdBy, Auction auction) {
		this.id = id;
		this.bidPrice = bidPrice;
		this.status = status;
		this.isDeleted = isDeleted;
		this.user = user;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.auction = auction;
	}

	public void delete() {
		this.isDeleted = true;
	}
}
