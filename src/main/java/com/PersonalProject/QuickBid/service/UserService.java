package com.PersonalProject.QuickBid.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.PersonalProject.QuickBid.model.Auction;
import com.PersonalProject.QuickBid.model.Users;
import com.PersonalProject.QuickBid.model.Users.Role;
import com.PersonalProject.QuickBid.repository.BidRepo;
import com.PersonalProject.QuickBid.repository.UsersRepo;

@Service
public class UserService {
     @Autowired
    private  BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private BidRepo bidRepo;
    
    @Autowired
    private UsersRepo usersRepo;

    public Optional<Users> findUserByEmail(String email){
        return usersRepo.findByEmail(email);
    }

    public Optional<Users> findUserById(Long id){
        return usersRepo.findById(id);
    }

    public Users registerUser(Users users) {
        if (users.getRole() == null) {
            users.setRole(Role.USER); // Set default role
        }
        System.out.println(users.getEmail()+""+users.getPassword());
         users.setPassword(passwordEncoder.encode(users.getPassword()));
        return usersRepo.save(users);
    }
    
public List<Auction> viewEnrolledAuctions(Long id){
    return bidRepo.findEnrolledAuctionsByUser(id);
}

}
