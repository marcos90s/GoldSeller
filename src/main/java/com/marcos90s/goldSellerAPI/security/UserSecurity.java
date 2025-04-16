package com.marcos90s.goldSellerAPI.security;

import com.marcos90s.goldSellerAPI.entities.Users;
import com.marcos90s.goldSellerAPI.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

    @Autowired
    private UsersRepository usersRepository;

    public boolean isCurrentUser(String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return false;
        }

        String email = authentication.getName();
        Users user = usersRepository.findByEmail(email).orElse(null);

        return user != null && user.getId().equals(userId);
    }
}
