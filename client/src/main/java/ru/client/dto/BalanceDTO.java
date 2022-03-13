package ru.client.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class BalanceDTO {
    private final String cardNumber;
    private final BigDecimal amount;

}
