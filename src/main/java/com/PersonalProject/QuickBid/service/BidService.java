package com.PersonalProject.QuickBid.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.PersonalProject.QuickBid.model.Auction;
import com.PersonalProject.QuickBid.model.Bid;
import com.PersonalProject.QuickBid.repository.AuctionRepo;
import com.PersonalProject.QuickBid.repository.BidRepo;
import com.PersonalProject.QuickBid.repository.UsersRepo;

@Service
public class BidService {
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UsersRepo usersRepo;
    
    @Autowired
    private BidRepo bidRepo;

    @Autowired
    private AuctionRepo auctionRepo;

    public Bid placeBid(Long auctionId , Long userId , BigDecimal bidAmount){

        Auction auction = auctionRepo.findById(auctionId)
                          .orElseThrow(()->new RuntimeException("Auction not Found"));

        if(auction.getStatus() == Auction.Status.CLOSED ){
            throw new RuntimeException("Auction is Closed");
        }

        if(bidAmount.compareTo(auction.getCurrentPrice())<=0 && bidAmount.compareTo(auction.getStartingPrice()) <= 0){
            throw new RuntimeException("Bid must be higher than the Current or Starting price.");
        }

        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setUser(usersRepo.findById(userId).get());
        bid.setBidAmount(bidAmount);
        bid.setBidTime(LocalDateTime.now());

        // Update auction price
        auction.setCurrentPrice(bidAmount);
        auctionRepo.save(auction);

        bidRepo.save(bid);

        // Sends Real time update to all user who are subscribed to this topic/bids/auctionid URL...
        simpMessagingTemplate.convertAndSend("/topic/bids/"+ auctionId , bid);

        return bid;
    }

    public List<Bid> getAllBidsForAuction(Long AuctionId){
          return bidRepo.findByAuctionId(AuctionId);
    }

    

}
