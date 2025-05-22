package com.marcos90s.goldSellerAPI.services;

import com.marcos90s.goldSellerAPI.dto.GameTransactionRequestDTO;
import com.marcos90s.goldSellerAPI.dto.GameTransactionResponseDTO;
import com.marcos90s.goldSellerAPI.entities.GameTransaction;
import com.marcos90s.goldSellerAPI.entities.Users;
import com.marcos90s.goldSellerAPI.exception.InternalServerErrorException;
import com.marcos90s.goldSellerAPI.exception.NotFoundException;
import com.marcos90s.goldSellerAPI.repository.GameTransactionRepository;
import com.marcos90s.goldSellerAPI.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameTransactionService {

    @Autowired
    GameTransactionRepository gameTransactionRepository;

    @Autowired
    UsersRepository usersRepository;

    public GameTransactionResponseDTO createTransaction(GameTransactionRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        GameTransaction tx = new GameTransaction();
        tx.setUser(user);
        tx.setType(dto.getType());
        tx.setAmount(dto.getAmount());
        tx.setQuantity(dto.getQuantity());
        tx.setItemName(dto.getItemName());
        tx.setDate(LocalDateTime.now());

        user.applyGameTransaction(dto.getAmount(), dto.getQuantity(), dto.getType().name());
        user.getGameTransactions().add(tx);

        try {
            usersRepository.save(user);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error while saving user!");
        }
        try {
            gameTransactionRepository.save(tx);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error while saving transaction!");
        }

        return mapToResponseDTO(tx);
    }

    public List<GameTransactionResponseDTO> getAllTransactions() {
        return gameTransactionRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<GameTransactionResponseDTO> getByUserId(String id){
        if(!usersRepository.existsById(id)){
            throw new NotFoundException("Not Found!");
        }
        return gameTransactionRepository.findByUserId(id).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public GameTransactionResponseDTO getTransactionById(String id) {
        GameTransaction tx = gameTransactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found!"));
        return mapToResponseDTO(tx);
    }

    @Transactional
    public void deleteTransaction(String id) {
        GameTransaction gameTransaction = gameTransactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found!"));

        Users user = gameTransaction.getUser();
        user.getGameTransactions().remove(gameTransaction);

        usersRepository.save(user);

    }

    private GameTransactionResponseDTO mapToResponseDTO(GameTransaction tx) {
        GameTransactionResponseDTO dto = new GameTransactionResponseDTO();
        dto.setId(tx.getId());
        dto.setUserId(tx.getUser().getId());
        dto.setType(tx.getType().name());
        dto.setAmount(tx.getAmount());
        dto.setQuantity(tx.getQuantity());
        dto.setItemName(tx.getItemName());
        dto.setLocalDateTime(tx.getDate());
        return dto;
    }

}
