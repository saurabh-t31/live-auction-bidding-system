package com.PersonalProject.QuickBid.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.PersonalProject.QuickBid.model.Auction;
import com.PersonalProject.QuickBid.model.Users;
import com.PersonalProject.QuickBid.service.AuctionService;
import com.PersonalProject.QuickBid.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/user")
public class UserController {

     @Autowired
     private AuctionService auctionService;

     @Autowired
     private UserService userService;

     @GetMapping("/winners")
     public String getAllPastAuctions(Model model){
        List<Auction> auction = auctionService.getInActiveAuction();
        model.addAttribute("winners", auction);
        return "user-winner-list";
     }

     @GetMapping("/enrolled-auctions")
     public String getAllEnrolledAuctions(Model model,Principal principal){
      String name = principal.getName(); 
      List<Auction> auction = userService.viewEnrolledAuctions(userService.findUserByEmail(name).get().getId());
      model.addAttribute("enrolledAuctions", auction);
      return "user-enrolled-list";
     }

}
