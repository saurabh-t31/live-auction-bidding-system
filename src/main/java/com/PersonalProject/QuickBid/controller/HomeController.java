package com.PersonalProject.QuickBid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.PersonalProject.QuickBid.model.Users;
import com.PersonalProject.QuickBid.service.UserService;

@Controller
public class HomeController {
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired 
    private UserService userService;
    
    @GetMapping("/register")
    public String registerationForm(Model model) {
        model.addAttribute("users", new Users());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute Users users) {
        System.out.println("User password:"+users.getPassword());
        userService.registerUser(users);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }

    @GetMapping("/dashboard")
    public String userDashboard(Model model) {
        return "user-dashboard"; 
    }
    

}
