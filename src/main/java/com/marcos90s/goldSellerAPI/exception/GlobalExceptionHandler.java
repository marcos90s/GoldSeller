package com.marcos90s.goldSellerAPI.exception;

import com.marcos90s.goldSellerAPI.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDTO> handlerNotFoundException(NotFoundException ex){

        ExceptionDTO exceptionDTO = ExceptionDTO.create(404, ex.getMessage());
        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDTO> handlerBadRequestException( BadRequestException ex){

        ExceptionDTO exceptionDTO = ExceptionDTO.create(404, ex.getMessage());
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ExceptionDTO> handlerServerError( InternalServerErrorException ex){

        ExceptionDTO exceptionDTO = ExceptionDTO.create(404, ex.getMessage());
        return new ResponseEntity<>(exceptionDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleOtherExceptions(Exception e) {
        ExceptionDTO exceptionDTO = ExceptionDTO.create(500 , "Unexpected error: " + e.getMessage()+"\n "+e.getClass()+ "\n"+e.getStackTrace().toString());
        return ResponseEntity.status(500).body(exceptionDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleValidation(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(field -> field.getField() + ": " + field.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ExceptionDTO exceptionDTO = ExceptionDTO.create(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionDTO> handleBadCredentials(BadCredentialsException ex){
        ExceptionDTO exceptionDTO = ExceptionDTO.create(401, ex.getMessage());
        return ResponseEntity.status(401).body(exceptionDTO);

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDTO> handleAccessDeniedException(AccessDeniedException ex){
        ExceptionDTO exceptionDTO = ExceptionDTO.create(401, ex.getMessage());
        return ResponseEntity.status(401).body(exceptionDTO);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionDTO> handleAuthDeniedException(AuthorizationDeniedException ex){
        ExceptionDTO exceptionDTO = ExceptionDTO.create(403, ex.getMessage());
        return ResponseEntity.status(403).body(exceptionDTO);
    }
}
