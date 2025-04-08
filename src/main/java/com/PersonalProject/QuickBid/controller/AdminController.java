package com.PersonalProject.QuickBid.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.PersonalProject.QuickBid.model.Auction;
import com.PersonalProject.QuickBid.model.Bid;
import com.PersonalProject.QuickBid.model.Users;
import com.PersonalProject.QuickBid.service.AuctionService;
import com.PersonalProject.QuickBid.service.BidService;
import com.PersonalProject.QuickBid.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private BidService bidService;
    
    @GetMapping("/dashboard")
    public String adminDashboard(Model model){
        return "admin-dashboard";
    }
    
    @GetMapping("/auctions")
    public String listAuctions(Model model){
         List<Auction> auctions = auctionService.getAllAuctions();
         model.addAttribute("auctions", auctions);
         return "admin-auction-list";
    }

    @GetMapping("/auction/new")
    public String showCreateAuctionForm(Model model){
         model.addAttribute("auction", new Auction());
         return "admin-auction-form";
    }

    @PostMapping("/auction/new")
    public String showCreateAuctionForm(@ModelAttribute Auction auction,Principal principal){

         Users admin = userService.findUserByEmail(principal.getName()).get();
         auction.setCreatedBy(admin);
         auctionService.createAuction(auction);

         return "redirect:/admin/auctions";
    }

    @GetMapping("/auction/update/{id}")
    public String updateAuctionForm(Model model, @PathVariable Long id){ 
        Auction auction = auctionService.getAuctionById(id);
        model.addAttribute( "auction",auction );
        return "admin-auction-form";
    }

    @GetMapping("/auction/close/{id}")
    public String closeAuction(@PathVariable Long id) {
        auctionService.closeAuction(id);
        return "redirect:/admin/auctions";
    }
    @GetMapping("/auction/delete/{id}")
    public String deleteAuction(@PathVariable Long id) {
        auctionService.deleteAuction(id);
        return "redirect:/admin/auctions";
    }

    @GetMapping("/winners")
    public String getAllWinners(Model model) {
        List<Auction> auction = auctionService.getInActiveAuction();
        model.addAttribute("auctions",auction );
        return "admin-winner-list";
    }
    @GetMapping("/live-auctions")
    public String showLiveAuctions(Model model) {
    List<Auction> liveAuctions = auctionService.getLiveAuction();
    model.addAttribute("liveAuctions", liveAuctions);
    return "admin-live-auctions";
}
@GetMapping("/inactive-auctions")
public String viewInactiveAuctions(Model model) {
    List<Auction> inactiveAuctions = auctionService.getInActiveAuction(); // Custom method
    model.addAttribute("auctions", inactiveAuctions);
    return "admin-inactive-auction"; // Template name
}
    
    @GetMapping("/auction-details/{id}")
    public String viewAuctionForAdmin(@PathVariable Long id, Model model) {
    Auction auction = auctionService.getAuctionById(id);
    List<Bid> bids = bidService.getAllBidsForAuction(id);
    model.addAttribute("auction", auction);
    model.addAttribute("bids", bids);
    return "admin-auction-details";
}
    

}
