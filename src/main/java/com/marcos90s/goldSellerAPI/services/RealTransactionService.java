package com.marcos90s.goldSellerAPI.services;

import com.marcos90s.goldSellerAPI.dto.RealTransactionRequestDTO;
import com.marcos90s.goldSellerAPI.dto.RealTransactionResponseDTO;
import com.marcos90s.goldSellerAPI.entities.RealTransaction;
import com.marcos90s.goldSellerAPI.entities.Users;
import com.marcos90s.goldSellerAPI.repository.RealTransactionRepository;
import com.marcos90s.goldSellerAPI.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RealTransactionService {

    @Autowired
    RealTransactionRepository realTransactionRepository;

    @Autowired
    UsersRepository usersRepository;

    public RealTransactionResponseDTO createTransaction(RealTransactionRequestDTO dto) {
        Users user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        RealTransaction tx = new RealTransaction();
        tx.setUser(user);
        tx.setAmount(dto.getAmount());
        tx.setAmountInGold(dto.getAmountInGold());
        tx.setCharName(dto.getCharName());
        tx.setDescription(dto.getDescription());
        tx.setDate(LocalDateTime.now());

        user.applyRealTransaction(dto.getAmount(), dto.getAmountInGold());
        user.getRealTransactions().add(tx);
        usersRepository.save(user);
        realTransactionRepository.save(tx);

        return mapToResponseDTO(tx);
    }

    public List<RealTransactionResponseDTO> getAllTransactions() {
        return realTransactionRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public RealTransactionResponseDTO getTransactionById(String id) {
        RealTransaction tx = realTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return mapToResponseDTO(tx);
    }

    public void deleteTransaction(String id) {
        if (!realTransactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found");
        }
        realTransactionRepository.deleteById(id);
    }

    private RealTransactionResponseDTO mapToResponseDTO(RealTransaction tx) {
        RealTransactionResponseDTO dto = new RealTransactionResponseDTO();
        dto.setId(tx.getId());
        dto.setUserId(tx.getUser().getId());
        dto.setAmount(tx.getAmount());
        dto.setCharName(tx.getCharName());
        dto.setDate(tx.getDate());
        dto.setDescription(tx.getDescription());
        return dto;
    }

}
