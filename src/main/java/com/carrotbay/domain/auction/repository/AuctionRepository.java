package com.carrotbay.domain.auction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.carrotbay.domain.auction.Auction;

import jakarta.persistence.LockModeType;

public interface AuctionRepository extends JpaRepository<Auction, Long>, AuctionCustomRepository {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM Auction a LEFT JOIN a.createdBy u WHERE a.id = :auctionId")
	Optional<Auction> getAuctionWithLock(@Param("auctionId") Long auctionId);
}
