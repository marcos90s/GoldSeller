package com.marcos90s.goldSellerAPI.controller;

import com.marcos90s.goldSellerAPI.dto.RealTransactionRequestDTO;
import com.marcos90s.goldSellerAPI.dto.RealTransactionResponseDTO;
import com.marcos90s.goldSellerAPI.services.RealTransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/real")
public class RealTransactionController {

    @Autowired
    RealTransactionService realTransactionService;

    @PostMapping
    public ResponseEntity<RealTransactionResponseDTO> create(@RequestBody @Valid RealTransactionRequestDTO dto) {
        RealTransactionResponseDTO response = realTransactionService.createTransaction(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RealTransactionResponseDTO>> getAll() {
        return ResponseEntity.ok(realTransactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RealTransactionResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(realTransactionService.getTransactionById(id));
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<List<RealTransactionResponseDTO>> getByUserId(@PathVariable String id){
        return ResponseEntity.ok(realTransactionService.getByUserId(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isUserFromRealTransact(#id, authentication.principal.username)")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        realTransactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }   
}
