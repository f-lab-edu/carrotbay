package com.carrotbay.domain.auction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.carrotbay.domain.auction.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

	// 메소드에 대해 JPQL 또는 SQL 쿼리를 직접 작성하여 데이터베이스와 상호작용한다.
	@Query("SELECT a FROM Auction a LEFT JOIN FETCH a.createdBy")
	public List<Auction> findAuctionList();

	@Query("SELECT a FROM Auction a LEFT JOIN FETCH a.createdBy WHERE a.id = :auctionId")
	public Auction findByAuctionId(@Param("auctionId") Long auctionId);

}
