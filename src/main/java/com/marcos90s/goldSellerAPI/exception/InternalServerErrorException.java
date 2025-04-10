package com.marcos90s.goldSellerAPI.exception;

public class InternalServerErrorException extends RuntimeException{
    public InternalServerErrorException(String msg){
        super(msg);
    }
}
