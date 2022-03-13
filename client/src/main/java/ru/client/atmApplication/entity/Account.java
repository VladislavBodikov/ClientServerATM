package ru.client.atmApplication.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private long id;
    private long userId;
    private String cardNumber;
    private String scoreNumber;
    private BigDecimal amount;
    private String pinCode;

    public Account(String cardNumber, String pinCode){
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
    }
}
