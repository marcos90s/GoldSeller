package com.marcos90s.goldSellerAPI.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class ExceptionDTO {
    private int status;
    private String message;
    private LocalDateTime date;

    private ExceptionDTO(int status, String message, LocalDateTime date) {
        this.status = status;
        this.message = message;
        this.date = date;
    }

    public static ExceptionDTO create(int status, String message) {
        return new ExceptionDTO(status, message, LocalDateTime.now());
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public  LocalDateTime getDate(){
        return date;
    }
}
