package ru.client.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountDTO {
    private String cardNumber;
    private String pinCode;
}
