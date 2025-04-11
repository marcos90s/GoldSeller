package com.marcos90s.goldSellerAPI.controller;

import com.marcos90s.goldSellerAPI.dto.UsersRequestDTO;
import com.marcos90s.goldSellerAPI.dto.UsersResponseDTO;
import com.marcos90s.goldSellerAPI.services.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<UsersResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UsersResponseDTO> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(usersService.getUserById(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        usersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UsersResponseDTO> updateUser(@PathVariable String id, @RequestBody UsersRequestDTO dto) {
        return ResponseEntity.ok(usersService.updateUser(id, dto));
    }

}
