package com.example.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class TestUserController {

    @PreAuthorize("hasAuthority('WEB_USER')")
    @GetMapping("/payment")
    public ResponseEntity<String> getTestUser(){
        return new ResponseEntity<>("Payment is completed", HttpStatus.OK);
    }
}
