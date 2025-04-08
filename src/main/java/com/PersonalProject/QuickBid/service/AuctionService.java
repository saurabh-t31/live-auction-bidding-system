package com.PersonalProject.QuickBid.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.PersonalProject.QuickBid.model.Auction;
import com.PersonalProject.QuickBid.model.Bid;
import com.PersonalProject.QuickBid.repository.AuctionRepo;
import com.PersonalProject.QuickBid.repository.BidRepo;

import jakarta.transaction.Transactional;

@Service
@EnableScheduling
public class AuctionService {
     
     @Autowired
     private SimpMessagingTemplate messagingTemplate;
    
     @Autowired
     private AuctionRepo auctionRepo;

     @Autowired
     private BidRepo bidRepo;

     public Auction createAuction(Auction auction){
      if (auction.getCurrentPrice() == null) {
         auction.setCurrentPrice(auction.getStartingPrice());
      } 
          return auctionRepo.save(auction);
     }

     public List<Auction> getAllAuctions(){
        return auctionRepo.findAll();
     }

     public List<Auction> getActiveAuction(){
        return auctionRepo.findByStatus(Auction.Status.ACTIVE);
     }
     public List<Auction> getLiveAuction(){
      return auctionRepo.findByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThan(Auction.Status.ACTIVE, LocalDateTime.now() , LocalDateTime.now() );
     }
     
     public List<Auction> getInActiveAuction(){
      return auctionRepo.findByStatus(Auction.Status.CLOSED);
   }

     public Auction getAuctionById(Long Id){
        return auctionRepo.findById(Id).get();
     }
     
     public void closeAuction(Long auctionId) {
        Optional<Auction> auctionOpt = auctionRepo.findById(auctionId);
        if (auctionOpt.isPresent()) {
            Auction auction = auctionOpt.get();
            auction.setStatus(Auction.Status.CLOSED);
            auctionRepo.save(auction);
        }
    }
   public void deleteAuction(Long id) {
      Optional<Auction> auctionOpt = auctionRepo.findById(id);
      if (auctionOpt.isPresent()) {
         auctionRepo.delete(auctionOpt.get());
      }
   }
  @Scheduled(fixedRate = 60000) // Runs every 1 minute
    public void closeExpiredAuctions() {
        List<Auction> expiredAuctions = auctionRepo.findByStatusAndEndTimeBefore(Auction.Status.ACTIVE, LocalDateTime.now());
        LocalDateTime now = LocalDateTime.now(); 
        for (Auction auction : expiredAuctions) {
         if (auction.getEndTime().isBefore(now)){

            Bid highestBid = bidRepo.findTopByAuctionOrderByBidAmountDesc(auction);

            if (highestBid != null) {
               auction.setWinner(highestBid.getUser());
           }

            auction.setStatus(Auction.Status.CLOSED);
            auctionRepo.save(auction);
            messagingTemplate.convertAndSend("/topic/auction-ended", auction.getId()); // Notify users

         }
        }
    }



}
