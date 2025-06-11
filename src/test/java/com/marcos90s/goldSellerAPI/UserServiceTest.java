package com.marcos90s.goldSellerAPI;

import com.marcos90s.goldSellerAPI.dto.UsersRequestDTO;
import com.marcos90s.goldSellerAPI.dto.UsersResponseDTO;
import com.marcos90s.goldSellerAPI.entities.Users;
import com.marcos90s.goldSellerAPI.enums.UserRole;
import com.marcos90s.goldSellerAPI.exception.BadRequestException;
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
import java.util.Arrays;
import java.util.List;

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

    private Users validUser1;
    private Users validUser2;
    private UsersRequestDTO validUserRequestDTO;
    private UsersResponseDTO validUserResponseDTO1;
    private UsersResponseDTO validUserResponseDTO2;

    @BeforeEach
    void setUp(){
        validUser1 = new Users(null, "Joãozinho",
                "joãozinho@email.com", "senhaCodificada123",
                UserRole.COMMON, null, null, new ArrayList<>(), new ArrayList<>());
        validUser2 = new Users(null, "Joãozin",
                "joãozin@email.com", "senhaCodificada123",
                UserRole.COMMON, null, null, new ArrayList<>(), new ArrayList<>());

        validUserRequestDTO = new UsersRequestDTO();
        validUserResponseDTO1 = new UsersResponseDTO();
        validUserResponseDTO2 = new UsersResponseDTO();

        validUserRequestDTO = createUserRequestDTO(validUser1.getName(),
                validUser1.getEmail(),
                "senha123",
                validUser1.getRole());

        validUserResponseDTO1 = createUserResponseDTO(validUser1.getId(),
                validUser1.getName(),
                validUser1.getEmail(),
                "COMMON");

        validUserResponseDTO2 = createUserResponseDTO(validUser2.getId(),
                validUser2.getName(), validUser2.getEmail(), "COMMON");


    }

    @Test
    @DisplayName("Deve cadastrar um usuário com sucesso")
    void deveCadastrarUsuarioERetornarDTOComSucesso(){

        when(passwordEncoder.encode(validUserRequestDTO.getPassword())).thenReturn("senhaCodificada123");
        //Quando a procura por email é chamada, retornamos false (cenário de sucesso).
        when(usersRepository.existsByEmail(validUserRequestDTO.getEmail())).thenReturn(false);
        //Quando salvamos qualquer Usuário do tipo User, retornamos um usuário válido (cenário de sucesso).
        when(usersRepository.save(any(Users.class))).thenReturn(validUser1);
        //Chamada do método de criação
        UsersResponseDTO responseDTO = usersService.createUser(validUserRequestDTO);

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getId());
        assertEquals("Joãozinho", responseDTO.getName());
        assertEquals("joãozinho@email.com", responseDTO.getEmail());
        assertEquals("COMMON", responseDTO.getRole());
        assertEquals(validUser1.getTotalGold(), responseDTO.getTotalGold());
        assertEquals(validUser1.getTotalMoney(), responseDTO.getTotalMoney());
        assertEquals(new ArrayList<>(), responseDTO.getGameTransactionIds());
        assertEquals(new ArrayList<>(), responseDTO.getRealTransactionIds());

        verify(usersRepository, times(1)).save(any(Users.class));

    }
    @Test
    @DisplayName("Deve lançar exceção quando email for existente")
    void deveLancarExcecaoParaEmailExistente(){
        //Chamar existsByEmail e retornar true;
        when(usersRepository.existsByEmail(validUserRequestDTO.getEmail())).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->{
            usersService.createUser(validUserRequestDTO);
        });
        assertEquals("Email is already exists!", exception.getMessage());
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    @DisplayName("Deve Retornar todos usuários como DTO")
    void deveRetornarTodosUsersComoDTO(){

        when(usersRepository.findAll()).thenReturn(Arrays.asList(validUser1, validUser2));

        List<UsersResponseDTO> actualUsers = usersService.getAllUsers();

        assertNotNull(actualUsers, "A Lista não deve ser nula");
        assertEquals(2, actualUsers.size());
        assertUserResponseDTOEquals(validUserResponseDTO1, actualUsers.get(0));
        assertUserResponseDTOEquals(validUserResponseDTO2, actualUsers.get(1));

        verify(usersRepository, times(1)).findAll();

    }

    private UsersResponseDTO createUserResponseDTO(String id, String name, String email, String role) {
        UsersResponseDTO dto = new UsersResponseDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setEmail(email);
        dto.setRole(role);
        dto.setTotalGold(null);
        dto.setTotalMoney(null);
        dto.setRealTransactionIds(new ArrayList<>());
        dto.setGameTransactionIds(new ArrayList<>());
        return dto;
    }

    private UsersRequestDTO createUserRequestDTO(String name, String email, String password, UserRole role){
        UsersRequestDTO dto = new UsersRequestDTO();
        dto.setName(name);
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setRole(role);
        return dto;
    }

    private void assertUserResponseDTOEquals(UsersResponseDTO expected, UsersResponseDTO actual){
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getRole(), actual.getRole());
        assertEquals(expected.getTotalGold(), actual.getTotalGold());
        assertEquals(expected.getTotalMoney(), actual.getTotalMoney());
        assertEquals(expected.getGameTransactionIds(), actual.getGameTransactionIds());
        assertEquals(expected.getRealTransactionIds(), actual.getRealTransactionIds());
    }
}
