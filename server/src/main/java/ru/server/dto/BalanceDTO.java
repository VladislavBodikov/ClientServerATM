package ru.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

@Data
public class BalanceDTO {
    private String cardNumber;
    private BigDecimal amount;
    private HttpStatus status;
    private String message;

}
