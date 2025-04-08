package com.PersonalProject.QuickBid.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.PersonalProject.QuickBid.model.Auction;
import com.PersonalProject.QuickBid.model.Bid;
import com.PersonalProject.QuickBid.model.Users;
import com.PersonalProject.QuickBid.service.AuctionService;
import com.PersonalProject.QuickBid.service.BidService;
import com.PersonalProject.QuickBid.service.UserService;

@Controller
@RequestMapping("/auctions")
@PreAuthorize("hasRole('USER')")
public class AuctionController {
        @Autowired
        private SimpMessagingTemplate messagingTemplate; // WebSocket Messaging

    
    @Autowired
    private AuctionService auctionService;
    
    @Autowired
    private BidService bidService;
    
    @Autowired
    private UserService userService;

    // List all active auctions
    @GetMapping
    public String listAuctions(Model model) {
        List<Auction> auctions = auctionService.getAllAuctions();
        model.addAttribute("auctions", auctions);
        return "auction-list";  // Template: auction-list.html
    }

    @GetMapping("/live-auctions")
    public String liveAuctions(Model model){
        List<Auction> auctions = auctionService.getLiveAuction();
        model.addAttribute("liveAuctions",auctions);
        return "user-live-auctions";
    }

    @GetMapping("/{id}")
    public String auctionDetail(@PathVariable Long id, Model model) {
        Auction auction = auctionService.getAuctionById(id);
        List<Bid> bids = bidService.getAllBidsForAuction(id);
        model.addAttribute("auction", auction);
        model.addAttribute("bids", bids);
        return "auction-detail"; // Template: auction-detail.html
    }

     @PostMapping("/{id}/bid")
    public String placeBid(@PathVariable Long id,@RequestParam BigDecimal bidAmount,Principal principal) {
        // Retrieve the authenticated user
        Users user = userService.findUserByEmail(principal.getName()).get();
        // Place bid using the service method
        bidService.placeBid(id, user.getId(), bidAmount);
        return "redirect:/auctions/" + id;

    }


}
