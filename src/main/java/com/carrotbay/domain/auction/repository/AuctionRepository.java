package com.carrotbay.domain.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carrotbay.domain.auction.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long>, AuctionCustomRepository {
}
