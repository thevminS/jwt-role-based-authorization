package com.example.demo.controllers;

import com.example.demo.dto.AuthenticationResponseDto;
import com.example.demo.dto.RefreshTokenRequestDto;
import com.example.demo.dto.UserDto;
import com.example.demo.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authenticationService.register(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authenticationService.login(userDto));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDto> getNewAccessToken(@RequestBody RefreshTokenRequestDto token) {
        return ResponseEntity.ok(authenticationService.getNewToken(token));
    }

    @PostMapping("/add-admins")
    @ResponseStatus(HttpStatus.OK)
    public void addAdmins() {
        authenticationService.addAdmin();
    }
}
