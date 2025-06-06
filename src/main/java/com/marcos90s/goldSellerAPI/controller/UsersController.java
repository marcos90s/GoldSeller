package com.marcos90s.goldSellerAPI.controller;

import com.marcos90s.goldSellerAPI.dto.UsersRequestDTO;
import com.marcos90s.goldSellerAPI.dto.UsersResponseDTO;
import com.marcos90s.goldSellerAPI.services.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UsersController {

    @Autowired
    UsersService usersService;

    @PostMapping
    public ResponseEntity<UsersResponseDTO> createUser(@RequestBody @Valid UsersRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.createUser(dto));
    }

    @GetMapping("/teste")
    public ResponseEntity<List<UsersResponseDTO>> getAllUsersTesteFront(){
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    //Only ADMIN
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsersResponseDTO>> getAllUsers(@RequestParam (name = "email", required = false) String email) {
        List<UsersResponseDTO> users;
        System.out.println("PARAMETRO RECEBIDO BACKEND: "+email
        );
        if(email != null && !email.trim().isEmpty()){
            System.out.println("GET BY EMAIL");
            users = usersService.getByEmail(email);
        }else {
            System.out.println("GET ALL");
            users = usersService.getAllUsers();
        }
        return ResponseEntity.ok(users);
    }
    //ADMIN and id that matches with current user id
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<UsersResponseDTO> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(usersService.getUserById(id));
    }

    @GetMapping(value = "search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsersResponseDTO>> getByEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(usersService.getByEmail(email));
    }

    //Only ADMIN
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        usersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    //ADMIN and id that matches with current user id
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<UsersResponseDTO> updateUser(@PathVariable String id, @RequestBody UsersRequestDTO dto) {
        return ResponseEntity.ok(usersService.updateUser(id, dto));
    }

}
