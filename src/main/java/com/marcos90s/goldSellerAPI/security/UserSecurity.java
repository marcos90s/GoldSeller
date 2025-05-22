package com.marcos90s.goldSellerAPI.security;

import com.marcos90s.goldSellerAPI.entities.GameTransaction;
import com.marcos90s.goldSellerAPI.entities.RealTransaction;
import com.marcos90s.goldSellerAPI.entities.Users;
import com.marcos90s.goldSellerAPI.repository.GameTransactionRepository;
import com.marcos90s.goldSellerAPI.repository.RealTransactionRepository;
import com.marcos90s.goldSellerAPI.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("userSecurity")
public class UserSecurity {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RealTransactionRepository realTransactionRepository;
    @Autowired
    private GameTransactionRepository gameTransactionRepository;

    public boolean isCurrentUser(String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return false;
        }

        String email = authentication.getName();
        Users user = usersRepository.findByEmail(email).orElse(null);

        return user != null && user.getId().equals(userId);
    }
    public boolean isUserFromRealTransact(String realTransactId, String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return false;
        }
        RealTransaction realTransaction = realTransactionRepository.findById(realTransactId).orElse(null);
        return realTransaction != null && Objects.equals(realTransaction.getUser().getEmail(), email);
    }

    public boolean isUserFromGameTransact(String gameTransactId, String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication ==null){
            return false;
        }
        GameTransaction gameTransaction = gameTransactionRepository.findById(gameTransactId).orElse(null);
        return gameTransaction != null && Objects.equals(gameTransaction.getUser().getEmail(), email);
    }
}
