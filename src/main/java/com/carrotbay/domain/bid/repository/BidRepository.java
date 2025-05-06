package com.carrotbay.domain.bid.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.bid.Bid;
import com.carrotbay.domain.user.User;

public interface BidRepository extends JpaRepository<Bid, Long>, BidCustomRepository {
	Optional<Bid> findByAuction(Auction auction);

	Optional<Bid> findByIdAndCreatedBy(Long auctionId, User createdBy);
}
