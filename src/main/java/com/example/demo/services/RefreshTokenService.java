package com.example.demo.services;

import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class RefreshTokenService {

    private static List<RefreshToken> TokenList = new ArrayList<>();
    //TODO
    private static final long REFRESH_TOKEN_EXPIRATION_TIME_MS = 86400000L;

    public RefreshToken createRefreshToken(User user){
        String userName = user.getEmail();
        Optional<RefreshToken> refreshToken= TokenList.stream().filter(refreshToken1 -> refreshToken1.getUser().getEmail().equals(userName)).findFirst();
        if (refreshToken.isPresent()){
            return refreshToken.get();
        }
        RefreshToken newRefreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRATION_TIME_MS))
                .build();
        TokenList.add(newRefreshToken);
        return newRefreshToken;
    }

    public RefreshToken verifyExpiration(String token){
        Optional<RefreshToken> refreshToken= TokenList.stream().filter(refreshToken1 -> refreshToken1.getToken().equals(token)).findFirst();
        if(refreshToken.isPresent()){
            if (refreshToken.get().getExpiryDate().compareTo(Instant.now()) < 0){
                TokenList.remove(refreshToken.get());
                throw new RuntimeException("Refresh token is expired. Please make a new login..!");
            }
            return refreshToken.get();
        }
        throw new RuntimeException("Invalid Token ... ");
    }

    public Optional<RefreshToken> findByToken(String token){
        return TokenList.stream().filter(refreshToken -> refreshToken.getToken().equals(token)).findFirst();
    }

    public Optional<RefreshToken> findByUser(User user){
        return TokenList.stream().filter(refreshToken -> refreshToken.getUser().equals(user)).findFirst();
    }
}
