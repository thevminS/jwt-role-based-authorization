package com.example.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public")
public class TestGuestController {

    @GetMapping("/get-quote")
    public ResponseEntity<String> getTestUser(){
        return new ResponseEntity<>("New quote", HttpStatus.OK);
    }
}
