package com.marcos90s.goldSellerAPI.services;

import com.marcos90s.goldSellerAPI.dto.GameTransactionRequestDTO;
import com.marcos90s.goldSellerAPI.dto.GameTransactionResponseDTO;
import com.marcos90s.goldSellerAPI.entities.GameTransaction;
import com.marcos90s.goldSellerAPI.entities.Users;
import com.marcos90s.goldSellerAPI.enums.GameTransactionType;
import com.marcos90s.goldSellerAPI.enums.UserRole;
import com.marcos90s.goldSellerAPI.exception.NotFoundException;
import com.marcos90s.goldSellerAPI.repository.GameTransactionRepository;
import com.marcos90s.goldSellerAPI.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameTransactionServiceTest {

    @Mock
    private GameTransactionRepository gameTransactionRepository;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private GameTransactionService gameTransactionService;

    private GameTransaction validGameTransaction;
    private GameTransactionRequestDTO validGameTransactionRequestDTO;
    private GameTransactionResponseDTO validGameTransactionResponseDTO;
    private Users validUser;

    @BeforeEach
    void setUp() {
        validUser = new Users(null, "Joãozinho",
                "joãozinho@email.com", "senhaCodificada123",
                UserRole.COMMON, null, null, new ArrayList<>(), new ArrayList<>());


        validGameTransaction = new GameTransaction(null, validUser, GameTransactionType.SALE,
                "Item 01", 40000, 1, LocalDateTime.parse("2025-03-03T10:15:30"));
    }

    @Test
    @DisplayName("Deve criar GameTransaction do tipo SALE e retornar DTO")
    void shouldCreateGameTransactionWithSALETYPEAndReturnDTO() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        when(authentication.getName()).thenReturn(validUser.getEmail());
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(usersRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.ofNullable(validUser));
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(gameTransactionRepository.save(any(GameTransaction.class))).thenReturn(validGameTransaction);

        validGameTransactionRequestDTO = createRequestDTO(validGameTransaction);

        gameTransactionService.createTransaction(validGameTransactionRequestDTO);

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        verify(usersRepository).save(userCaptor.capture());

        Users savedUser = userCaptor.getValue();
        int expectedTotalGold = (int) (validGameTransaction.getAmount() * validGameTransaction.getQuantity() * 0.95);

        assertEquals(expectedTotalGold, savedUser.getTotalGold());
        assertEquals(1, savedUser.getGameTransactions().size());

        verify(gameTransactionRepository, times(1)).save(any(GameTransaction.class));

    }

    @Test
    @DisplayName("Deve criar GameTransaction do tipo PURCHASE e retornar DTO")
    void shouldCreateGameTransactionWithPURCHASTYPEAndReturnDTO(){
        validGameTransaction.setType(GameTransactionType.PURCHASE);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        when(authentication.getName()).thenReturn(validUser.getEmail());
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(usersRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.ofNullable(validUser));
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(gameTransactionRepository.save(any(GameTransaction.class))).thenReturn(validGameTransaction);

        validGameTransactionRequestDTO = createRequestDTO(validGameTransaction);

        gameTransactionService.createTransaction(validGameTransactionRequestDTO);

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        verify(usersRepository).save(userCaptor.capture());

        Users savedUser = userCaptor.getValue();
        int expectedTotalGold = -validGameTransaction.getAmount() * validGameTransaction.getQuantity();

        assertEquals(expectedTotalGold, savedUser.getTotalGold());
        assertEquals(1, savedUser.getGameTransactions().size());

        verify(gameTransactionRepository, times(1)).save(any(GameTransaction.class));

    }

    @Test
    @DisplayName("Deve lançar exceção se usuário não for encontrado")
    void shouldThrowNotFoundExceptionWhenEmailNotFound(){
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        when(authentication.getName()).thenReturn(validUser.getEmail());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        GameTransactionRequestDTO reqDto = new GameTransactionRequestDTO();

        NotFoundException exception = assertThrows(NotFoundException.class, ()->{
            gameTransactionService.createTransaction(reqDto);
        });

        assertEquals("User not found", exception.getMessage());

        verify(usersRepository, never()).save(any());
        verify(gameTransactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve")
    void getAllTransactions() {
        Users validUser2 = new Users(null, "jão", "jão@jão.com", "senhe321",
                UserRole.COMMON, null, null, new ArrayList<>(), new ArrayList<>());

        GameTransaction validGameTransaction2 = new GameTransaction(null, validUser2, GameTransactionType.SALE,
                "Item 01", 40000, 1, LocalDateTime.parse("2025-03-03T10:15:30"));

        GameTransactionResponseDTO validGameTransactionResponseDTO = createResponseDTO(validGameTransaction);
        GameTransactionResponseDTO validGameTransactionResponseDTO2 = createResponseDTO(validGameTransaction2);

        when(gameTransactionRepository.findAll()).thenReturn(Arrays.asList(validGameTransaction, validGameTransaction2));
        List<GameTransactionResponseDTO> actualGameTransactions = gameTransactionService.getAllTransactions();

        assertNotNull(actualGameTransactions);
        assertEquals(2, actualGameTransactions.size());

        assertGameTransactionResponseDTOEquals(validGameTransactionResponseDTO, actualGameTransactions.get(0));
        assertGameTransactionResponseDTOEquals(validGameTransactionResponseDTO2, actualGameTransactions.get(1));

        verify(gameTransactionRepository, times(1)).findAll();

    }

    @Test
    void getByUserId() {
    }

    @Test
    void getTransactionById() {
    }

    @Test
    void deleteTransaction() {
    }

    public GameTransactionRequestDTO createRequestDTO(GameTransaction tx){
        GameTransactionRequestDTO reqDto = new GameTransactionRequestDTO();
        reqDto.setAmount(tx.getAmount());
        reqDto.setQuantity(tx.getQuantity());
        reqDto.setType(tx.getType());
        reqDto.setItemName(tx.getItemName());

        return reqDto;
    }

    public GameTransactionResponseDTO createResponseDTO(GameTransaction tx){
        GameTransactionResponseDTO resDto = new GameTransactionResponseDTO();
        resDto.setId(tx.getId());
        resDto.setUserId(tx.getUser().getId());
        resDto.setType(tx.getType().toString());
        resDto.setAmount(tx.getAmount());
        resDto.setQuantity(tx.getQuantity());
        resDto.setItemName(tx.getItemName());

        return resDto;
    }

    public void assertGameTransactionResponseDTOEquals(GameTransactionResponseDTO expected, GameTransactionResponseDTO actual){
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getQuantity(), actual.getQuantity());
        assertEquals(expected.getItemName(), actual.getItemName());
    }

}