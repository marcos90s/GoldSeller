package com.marcos90s.goldSellerAPI.services;

import com.marcos90s.goldSellerAPI.dto.UsersRequestDTO;
import com.marcos90s.goldSellerAPI.dto.UsersResponseDTO;
import com.marcos90s.goldSellerAPI.entities.Users;
import com.marcos90s.goldSellerAPI.enums.UserRole;
import com.marcos90s.goldSellerAPI.exception.BadRequestException;
import com.marcos90s.goldSellerAPI.exception.InternalServerErrorException;
import com.marcos90s.goldSellerAPI.exception.NotFoundException;
import com.marcos90s.goldSellerAPI.repository.UsersRepository;
import com.marcos90s.goldSellerAPI.utils.Utils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsersResponseDTO createUser(UsersRequestDTO dto) {
        if (usersRepository.existsByEmail(dto.getEmail())){
            throw new BadRequestException("Email is already exists!");
        }
        Users user = new Users();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null ? dto.getRole() : UserRole.COMMON);

        try {
            usersRepository.save(user);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error while saving user!");
        }
        return mapToResponseDTO(user);
    }

    public List<UsersResponseDTO> getAllUsers() {
        return usersRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UsersResponseDTO> getByEmail(String email){
        List<UsersResponseDTO> listUserDto = new ArrayList<>();
        List<Users> users = usersRepository.findUsersByEmailContaining(email);

        if(users.isEmpty()){
            System.out.println("User not Found. Throwing exception...");
            throw new NotFoundException("Users with email: "+email + " not Found");
        }
        for (Users user : users){
           listUserDto.add(mapToResponseDTO(user));
        }
        return listUserDto;
    }

    @Transactional
    public UsersResponseDTO getUserById(String id) {
        Users user = usersRepository.findByIdWithTransactions(id)
                .orElseThrow(() -> new NotFoundException("User not Found!"));

        return mapToResponseDTO(user);
    }

    public void deleteById(String id) {
        if (!usersRepository.existsById(id)){
            throw new NotFoundException("User not Found!");
        }
        try {
            usersRepository.deleteById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error while deleting user!");
        }
    }

    public UsersResponseDTO updateUser(String id, UsersRequestDTO dto) {
        if(!usersRepository.existsById(id)){
            throw new NotFoundException("User not Found!");
        }
        Users entity = usersRepository.getReferenceById(id);
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getPassword() != null) entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        try {
            usersRepository.save(entity);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error while saving user!");
        }
        return mapToResponseDTO(entity);
    }

    private UsersResponseDTO mapToResponseDTO(Users user) {
        UsersResponseDTO dto = new UsersResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setTotalGold(user.getTotalGold());
        dto.setTotalMoney(user.getTotalMoney());
        dto.setRealTransactionIds(Utils.makeRealTransactionInfo(user.getRealTransactions()));
        dto.setGameTransactionIds(Utils.makeGameTransactionInfo(user.getGameTransactions()));
        return dto;
    }
}
