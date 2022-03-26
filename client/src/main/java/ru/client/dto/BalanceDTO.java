package ru.client.dto;

import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;

@Data
public class BalanceDTO {
    private String cardNumber;
    private BigDecimal amount;

}
