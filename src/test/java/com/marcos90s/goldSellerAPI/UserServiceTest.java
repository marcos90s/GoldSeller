package com.marcos90s.goldSellerAPI;

import com.marcos90s.goldSellerAPI.dto.UsersRequestDTO;
import com.marcos90s.goldSellerAPI.dto.UsersResponseDTO;
import com.marcos90s.goldSellerAPI.entities.Users;
import com.marcos90s.goldSellerAPI.enums.UserRole;
import com.marcos90s.goldSellerAPI.repository.UsersRepository;
import com.marcos90s.goldSellerAPI.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //Integração Mockito com JUnit 5
public class UserServiceTest {

    private String randomUUID;

    @Mock //Mock da interface UsersRepository
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsersService usersService;

    private Users validUser;
    private UsersRequestDTO validUserRequestDTO;
    private UsersResponseDTO validUserResponseDTO;

    @BeforeEach
    void setUp(){
        validUser = new Users(null, "Joãozinho",
                "joãozinho@email.com", "senhaCodificada123",
                UserRole.COMMON, null, null, new ArrayList<>(), new ArrayList<>());

        validUserRequestDTO = new UsersRequestDTO();
        validUserResponseDTO = new UsersResponseDTO();

        validUserRequestDTO.setName(validUser.getName());
        validUserRequestDTO.setEmail(validUser.getEmail());
        validUserRequestDTO.setPassword("senha123");
        validUserRequestDTO.setRole(validUser.getRole());

        validUserResponseDTO.setId(validUser.getId());
        validUserResponseDTO.setName(validUser.getName());
        validUserResponseDTO.setEmail(validUser.getEmail());
        validUserResponseDTO.setRole("COMMON");
        validUserResponseDTO.setTotalGold(validUser.getTotalGold());
        validUserResponseDTO.setTotalMoney(validUser.getTotalMoney());
        validUserResponseDTO.setRealTransactionIds(new ArrayList<>());
        validUserResponseDTO.setGameTransactionIds(new ArrayList<>());

        when(passwordEncoder.encode(validUserRequestDTO.getPassword())).thenReturn("senhaCodificada123");
    }

    @Test
    @DisplayName("Deve cadastrar um usuário com sucesso")
    void deveCadastrarUsuarioERetornarDTOComSucesso(){

        when(usersRepository.existsByEmail(validUserRequestDTO.getEmail())).thenReturn(false);

        when(usersRepository.save(any(Users.class))).thenReturn(validUser);

        UsersResponseDTO responseDTO = usersService.createUser(validUserRequestDTO);

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getId());
        assertEquals("Joãozinho", responseDTO.getName());
        assertEquals("joãozinho@email.com", responseDTO.getEmail());
        assertEquals("COMMON", responseDTO.getRole());
        assertEquals(responseDTO.getTotalGold(), validUser.getTotalGold());
        assertEquals(responseDTO.getTotalMoney(), validUser.getTotalMoney());
        assertEquals(new ArrayList<>(), responseDTO.getGameTransactionIds());
        assertEquals(new ArrayList<>(), responseDTO.getRealTransactionIds());

        verify(usersRepository, times(1)).save(any(Users.class));

    }
}
