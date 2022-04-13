package ru.common.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

@Data
public class BalanceDTO {
    private String cardNumber;
    private BigDecimal amount;
    private HttpStatus status;
    private String message;

}
