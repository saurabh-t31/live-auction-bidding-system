package com.PersonalProject.QuickBid.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.PersonalProject.QuickBid.model.Users;
import com.PersonalProject.QuickBid.repository.UsersRepo;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Email being searched: " + email);

       Users users = usersRepo.findByEmail(email)
       .orElseThrow(()-> new UsernameNotFoundException("User not Found"));
       System.out.println(users.getPassword());
       
       return User.builder()
       .username(users.getEmail())
       .password(users.getPassword())
       .roles(users.getRole().name().replace("ROLE_", ""))  // Convert ENUM to role name
       .build();
   
    }
    
}
