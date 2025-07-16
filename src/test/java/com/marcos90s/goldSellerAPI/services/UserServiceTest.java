package com.marcos90s.goldSellerAPI.services;

import com.marcos90s.goldSellerAPI.dto.UsersRequestDTO;
import com.marcos90s.goldSellerAPI.dto.UsersResponseDTO;
import com.marcos90s.goldSellerAPI.entities.Users;
import com.marcos90s.goldSellerAPI.enums.UserRole;
import com.marcos90s.goldSellerAPI.exception.BadRequestException;
import com.marcos90s.goldSellerAPI.exception.NotFoundException;
import com.marcos90s.goldSellerAPI.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
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

    }

    @Test
    @DisplayName("Deve cadastrar um usuário com sucesso")
    void shouldCreateUserAndReturnDto(){
        validUserRequestDTO = new UsersRequestDTO();
        validUserRequestDTO = createUserRequestDTO(validUser1);

        when(passwordEncoder.encode(validUserRequestDTO.getPassword())).thenReturn("senhaCodificada123");
        when(usersRepository.existsByEmail(validUserRequestDTO.getEmail())).thenReturn(false);
        when(usersRepository.save(any(Users.class))).thenReturn(validUser1);
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
    void shouldThrowExceptionForExistingEmail(){
        validUserRequestDTO = new UsersRequestDTO();
        validUserRequestDTO = createUserRequestDTO(validUser1);

        when(usersRepository.existsByEmail(validUserRequestDTO.getEmail())).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->{
            usersService.createUser(validUserRequestDTO);
        });
        assertEquals("Email is already exists!", exception.getMessage());
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    @DisplayName("Deve Retornar todos usuários como DTO")
    void shouldReturnAllUsersDto(){
        validUser2 = new Users(null, "Joãozin",
                "joãozin@email.com", "senhaCodificada123",
                UserRole.COMMON, null, null, new ArrayList<>(), new ArrayList<>());

        validUserResponseDTO1 = new UsersResponseDTO();
        validUserResponseDTO1 = createUserResponseDTO(validUser1);

        validUserResponseDTO2 = new UsersResponseDTO();
        validUserResponseDTO2 = createUserResponseDTO(validUser2);


        when(usersRepository.findAll()).thenReturn(Arrays.asList(validUser1, validUser2));

        List<UsersResponseDTO> actualUsers = usersService.getAllUsers();

        assertNotNull(actualUsers, "A Lista não deve ser nula");
        assertEquals(2, actualUsers.size());
        assertUserResponseDTOEquals(validUserResponseDTO1, actualUsers.get(0));
        assertUserResponseDTOEquals(validUserResponseDTO2, actualUsers.get(1));

        verify(usersRepository, times(1)).findAll();

    }

    @Test
    @DisplayName("Deve Retornar lista de users contendo string no email")
    void shouldSearchByEmailAndReturnDto(){
        validUser2 = new Users(null, "Joãozin",
                "joãozin@email.com", "senhaCodificada123",
                UserRole.COMMON, null, null, new ArrayList<>(), new ArrayList<>());

        validUserResponseDTO1 = new UsersResponseDTO();
        validUserResponseDTO1 = createUserResponseDTO(validUser1);

        validUserResponseDTO2 = new UsersResponseDTO();
        validUserResponseDTO2 = createUserResponseDTO(validUser2);

        String emailToFind = "joãoz";
        when(usersRepository.findUsersByEmailContaining(emailToFind)).thenReturn(Arrays.asList(validUser1, validUser2));

        List<UsersResponseDTO> foundedUsers = usersService.getByEmail(emailToFind);
        assertNotNull(foundedUsers, "A lista não deve ser nula");
        assertEquals(2, foundedUsers.size());
        assertUserResponseDTOEquals(validUserResponseDTO1, foundedUsers.get(0));
        assertUserResponseDTOEquals(validUserResponseDTO2, foundedUsers.get(1));

        verify(usersRepository, times(1)).findUsersByEmailContaining(emailToFind);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a lista for vazia")
    void shouldThrowExceptionWhenEmptyList(){
        String emailToFind = "joãoz";
        when(usersRepository.findUsersByEmailContaining(emailToFind)).thenReturn(Collections.emptyList());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                usersService.getByEmail(emailToFind));
        assertEquals("Users with email: "+emailToFind + " not Found", exception.getMessage());
        verify(usersRepository, times(1)).findUsersByEmailContaining(emailToFind);
    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void shouldDeleteById(){
        when(usersRepository.existsById(validUser1.getId())).thenReturn(true);
        doNothing().when(usersRepository).deleteById(validUser1.getId());

        assertDoesNotThrow(() -> usersService.deleteById(validUser1.getId()));
        verify(usersRepository, times(1)).existsById(validUser1.getId());
        verify(usersRepository, times(1)).deleteById(validUser1.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção quando id não existir")
    void shouldThrowExceptionIfIdNotExists(){
        when(usersRepository.existsById(validUser1.getId())).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                usersService.deleteById(validUser1.getId()));

        assertEquals("User not Found!", exception.getMessage());
        verify(usersRepository, times(1)).existsById(validUser1.getId());
    }

    @Test
    @DisplayName("Deve alterar usuário com sucesso")
    void shouldAlterUserWithSuccessAndReturnDto(){
        when(usersRepository.existsById(validUser1.getId())).thenReturn(true);
        when(usersRepository.getReferenceById(validUser1.getId())).thenReturn(validUser1);

        UsersRequestDTO requestDTO = new UsersRequestDTO();
        requestDTO.setName("joão atualizado");
        requestDTO.setPassword("novaSenhaDoJoãozinho");

        String encryptedPasswordMock = "senha_codificada_super_dificil";
        when(passwordEncoder.encode("novaSenhaDoJoãozinho")).thenReturn(encryptedPasswordMock);

        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsersResponseDTO responseDTO = usersService.updateUser(validUser1.getId(), requestDTO);

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);

        verify(usersRepository, times(1)).save(userCaptor.capture());
        Users savedUser = userCaptor.getValue();

        assertEquals("joão atualizado", savedUser.getName());
        assertEquals(encryptedPasswordMock, savedUser.getPassword());
        assertNotNull(responseDTO);
        verify(passwordEncoder, times(1)).encode(requestDTO.getPassword());

    }

    @Test
    @DisplayName("Deve alterar apenas o nome do usuário quando a senha for nula")
    void shouldAlterOnlyUserNameWhenPasswordIsNull(){
        when(usersRepository.existsById(validUser1.getId())).thenReturn(true);
        when(usersRepository.getReferenceById(validUser1.getId())).thenReturn(validUser1);

        UsersRequestDTO requestDTO = new UsersRequestDTO();
        requestDTO.setName("joão atualizado");

        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UsersResponseDTO responseDTO = usersService.updateUser(validUser1.getId(), requestDTO);

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);

        verify(usersRepository, times(1)).save(userCaptor.capture());
        Users savedUser = userCaptor.getValue();

        assertEquals("joão atualizado", savedUser.getName());
        assertEquals(validUser1.getPassword(), savedUser.getPassword());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("Deve alterar apenas a senha quando o nome for nulo")
    void shouldAlterOnlyPasswordWhenNameIsNull(){
        when(usersRepository.existsById(validUser1.getId())).thenReturn(true);
        when(usersRepository.getReferenceById(validUser1.getId())).thenReturn(validUser1);

        UsersRequestDTO requestDTO = new UsersRequestDTO();
        requestDTO.setPassword("novaSenhaDoJoãozinho");

        String encryptedPasswordMock = "senha_codificada_super_dificil";
        when(passwordEncoder.encode("novaSenhaDoJoãozinho")).thenReturn(encryptedPasswordMock);

        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsersResponseDTO responseDTO = usersService.updateUser(validUser1.getId(), requestDTO);

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);

        verify(usersRepository, times(1)).save(userCaptor.capture());
        Users savedUser = userCaptor.getValue();
        assertEquals(validUser1.getName(), savedUser.getName());
        assertEquals(encryptedPasswordMock, savedUser.getPassword());
        verify(passwordEncoder, times(1)).encode(requestDTO.getPassword());


    }

    private UsersResponseDTO createUserResponseDTO(Users user) {
        UsersResponseDTO dto = new UsersResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().toString());
        dto.setTotalGold(null);
        dto.setTotalMoney(null);
        dto.setRealTransactionIds(new ArrayList<>());
        dto.setGameTransactionIds(new ArrayList<>());
        return dto;
    }

    private UsersRequestDTO createUserRequestDTO(Users user){
        UsersRequestDTO dto = new UsersRequestDTO();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
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
