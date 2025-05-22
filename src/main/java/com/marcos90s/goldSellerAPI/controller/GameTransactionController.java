package com.marcos90s.goldSellerAPI.controller;

import com.marcos90s.goldSellerAPI.dto.GameTransactionRequestDTO;
import com.marcos90s.goldSellerAPI.dto.GameTransactionResponseDTO;
import com.marcos90s.goldSellerAPI.services.GameTransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/game")
public class GameTransactionController {

    @Autowired
    GameTransactionService gameTransactionService;

    @PostMapping
    public ResponseEntity<GameTransactionResponseDTO> create(@RequestBody @Valid GameTransactionRequestDTO dto) {
        GameTransactionResponseDTO response = gameTransactionService.createTransaction(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<GameTransactionResponseDTO>> getAll() {
        return ResponseEntity.ok(gameTransactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GameTransactionResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(gameTransactionService.getTransactionById(id));
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<List<GameTransactionResponseDTO>> getByUserId(@PathVariable String id){
        return ResponseEntity.ok(gameTransactionService.getByUserId(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isUserFromGameTransact(#id, authentication.principal.username)")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        gameTransactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
