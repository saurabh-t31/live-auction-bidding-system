package com.PersonalProject.QuickBid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PersonalProject.QuickBid.model.Auction;
import com.PersonalProject.QuickBid.model.Bid;

@Repository
public interface BidRepo extends JpaRepository<Bid,Long> {
    List<Bid> findByAuctionId(Long auctionId);
    Bid findTopByAuctionOrderByBidAmountDesc(Auction auction);

    //JPQL works with entities and their fields, not actual table or column names.
    /* Bid here is your entity class, not your database table.
      b.auction refers to the auction field in the Bid entity, which is a reference (ManyToOne) to an Auction entity.*/
      
    @Query("SELECT DISTINCT b.auction FROM Bid b WHERE b.user.id = :userId")
    List<Auction> findEnrolledAuctionsByUser(@Param("userId") Long userId);

}
