package com.marcos90s.goldSellerAPI.services;

import com.marcos90s.goldSellerAPI.dto.AuthRequestDTO;
import com.marcos90s.goldSellerAPI.dto.AuthResponseDTO;
import com.marcos90s.goldSellerAPI.dto.UsersRequestDTO;
import com.marcos90s.goldSellerAPI.dto.UsersResponseDTO;
import com.marcos90s.goldSellerAPI.entities.Users;
import com.marcos90s.goldSellerAPI.enums.UserRole;
import com.marcos90s.goldSellerAPI.security.JwtService;
import com.marcos90s.goldSellerAPI.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UsersService usersService;

    public AuthResponseDTO authenticate(AuthRequestDTO request){
        System.out.println("Authenticate method --- request: "+request.toString());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        System.out.println("Authentication object: "+ authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String jwt = jwtService.generateToken(userDetails);
        Users user = userDetailsService.getUserByEmail(request.getEmail());

        return new AuthResponseDTO(
                jwt,
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    public UsersResponseDTO register(UsersRequestDTO request){
        request.setRole(UserRole.COMMON);
        return usersService.createUser(request);
    }

}
