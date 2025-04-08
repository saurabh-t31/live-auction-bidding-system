package com.PersonalProject.QuickBid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PersonalProject.QuickBid.model.Auction;
import java.time.LocalDateTime;


@Repository
public interface AuctionRepo extends JpaRepository<Auction,Long> {
    List<Auction> findByStatus(Auction.Status status); 
    List<Auction> findByStatusAndEndTimeBefore(Auction.Status status, LocalDateTime now);
    //Live Auctions
    List<Auction> findByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThan(Auction.Status status,LocalDateTime now1,LocalDateTime now2);

}

