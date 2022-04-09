package ru.server.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDTO {

    private AccountDTO accountFrom;
    private String cardNumberTo;

    private BigDecimal amountToTransfer;
}
