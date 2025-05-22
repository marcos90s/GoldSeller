package com.marcos90s.goldSellerAPI.services;

import com.marcos90s.goldSellerAPI.dto.RealTransactionRequestDTO;
import com.marcos90s.goldSellerAPI.dto.RealTransactionResponseDTO;
import com.marcos90s.goldSellerAPI.entities.RealTransaction;
import com.marcos90s.goldSellerAPI.entities.Users;
import com.marcos90s.goldSellerAPI.exception.InternalServerErrorException;
import com.marcos90s.goldSellerAPI.exception.NotFoundException;
import com.marcos90s.goldSellerAPI.repository.RealTransactionRepository;
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
public class RealTransactionService {

    @Autowired
    RealTransactionRepository realTransactionRepository;

    @Autowired
    UsersRepository usersRepository;

    public RealTransactionResponseDTO createTransaction(RealTransactionRequestDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        RealTransaction tx = new RealTransaction();
        tx.setUser(user);
        tx.setAmount(dto.getAmount());
        tx.setAmountInGold(dto.getAmountInGold());
        tx.setCharName(dto.getCharName());
        tx.setDescription(dto.getDescription());
        tx.setDate(LocalDateTime.now());

        user.applyRealTransaction(dto.getAmount(), dto.getAmountInGold());
        user.getRealTransactions().add(tx);
        try {
            usersRepository.save(user);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error while saving user!");
        }
        try {
            realTransactionRepository.save(tx);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error while saving transaction!");
        }

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
                .orElseThrow(() -> new NotFoundException("Transaction not found!"));
        return mapToResponseDTO(tx);
    }

    public List<RealTransactionResponseDTO> getByUserId(String id){
        if(!usersRepository.existsById(id)){
            throw new NotFoundException("Not Found!");
        }
        return realTransactionRepository.findByUserId(id)
                .stream().map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTransaction(String id) {
        RealTransaction realTransaction = realTransactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found!"));

        Users user = realTransaction.getUser();
        user.getRealTransactions().remove(realTransaction);

        usersRepository.save(user);
    }

    private RealTransactionResponseDTO mapToResponseDTO(RealTransaction tx) {
        RealTransactionResponseDTO dto = new RealTransactionResponseDTO();
        dto.setId(tx.getId());
        dto.setUserId(tx.getUser().getId());
        dto.setAmount(tx.getAmount());
        dto.setAmountInGold(tx.getAmountInGold());
        dto.setCharName(tx.getCharName());
        dto.setDate(tx.getDate());
        dto.setDescription(tx.getDescription());
        return dto;
    }

}
