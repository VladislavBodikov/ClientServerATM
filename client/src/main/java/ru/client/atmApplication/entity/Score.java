package ru.client.atmApplication.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Score {
    private long id;
    private long userId;
    private String cardNumber;
    private String scoreNumber;
    private BigDecimal amount;
    private String pinCode;

    public Score(String cardNumber, String pinCode){
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
    }
}
