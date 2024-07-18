package com.example.demo.services;

import com.example.demo.dto.AuthenticationResponseDto;
import com.example.demo.dto.RefreshTokenRequestDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private static List<User> userList = new ArrayList<>();



    public AuthenticationResponseDto register(UserDto userDto) {
        Optional<User> user = userList.stream().filter(user1 -> user1.getEmail().equals(userDto.getEmail())).findFirst();
        if (user.isPresent()){
            throw new RuntimeException("User already exists");
        }
        Set<String> roles = new HashSet<>();
        roles.add("WEB_USER");
        User newuser = User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(roles)
                .build();
        userList.add(newuser);
        return createRequest(newuser, null);
    }


    public AuthenticationResponseDto login(UserDto userDto) {
        Optional<User> user = userList.stream().filter(user1 -> user1.getEmail().equals(userDto.getEmail())).findFirst();
        if (user.isEmpty()){
            throw new RuntimeException("User does not exists");
        }
        Optional<RefreshToken> token = refreshTokenService.findByUser(user.get());
        if (token.isPresent()){
            try{
                RefreshToken refreshToken = refreshTokenService.verifyExpiration(token.get().getToken());
                return createRequest(user.get(), refreshToken);
            }catch (RuntimeException e){

            }
        }
        return createRequest(user.get(), null);
    }

    private AuthenticationResponseDto createRequest(User user, RefreshToken token){
        var jwtToken = jwtService.generateToken(user);
        RefreshToken refreshToken;
        if (token == null){
            refreshToken=refreshTokenService.createRefreshToken(user);
        }else {
            refreshToken = token;
        }
        Date tokenExpiration = jwtService.tokenExpiration(jwtToken);

        return AuthenticationResponseDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(tokenExpiration).build();

    }

    public AuthenticationResponseDto getNewToken(RefreshTokenRequestDto token) {
        RefreshToken refreshToken = refreshTokenService.verifyExpiration(token.getToken());
        User user = refreshToken.getUser();
        return createRequest(user, refreshToken);

    }

    public static Optional<User> findByEmail(String email){
        return userList.stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    public void addAdmin() {
        Set<String> roles = new HashSet<>();
        roles.add("WEB_USER");
        roles.add("ADMIN");
        User newuser = User.builder()
                .email("admin")
                .password("abc123")
                .roles(roles)
                .build();
        userList.add(newuser);

        Set<String> roles2 = new HashSet<>();
        roles2.add("ADMIN");
        User newuser2 = User.builder()
                .email("admin2")
                .password("abc123")
                .roles(roles2)
                .build();
        userList.add(newuser2);
    }
}
