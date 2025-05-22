package com.marcos90s.goldSellerAPI.controller;

import com.marcos90s.goldSellerAPI.dto.AuthRequestDTO;
import com.marcos90s.goldSellerAPI.dto.AuthResponseDTO;
import com.marcos90s.goldSellerAPI.dto.UsersRequestDTO;
import com.marcos90s.goldSellerAPI.dto.UsersResponseDTO;
import com.marcos90s.goldSellerAPI.exception.InternalServerErrorException;
import com.marcos90s.goldSellerAPI.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        try {
           return ResponseEntity.ok(authService.authenticate(request));
        } catch (BadCredentialsException e) {
            throw  new BadCredentialsException("Email or password incorrect.");
        } catch (Exception e){
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UsersResponseDTO> register(@Valid @RequestBody UsersRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }
}
