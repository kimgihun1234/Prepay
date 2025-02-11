package com.d111.PrePay.exception;

public class NotEnoughBalanceException extends RuntimeException{
    public NotEnoughBalanceException(String message){super(message);}
}
