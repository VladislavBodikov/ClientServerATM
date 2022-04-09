package ru.server.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class AccountDTO {
    @Pattern(regexp = "^[\\d]{16}$")
    private String cardNumber;
    @Pattern(regexp = "^[\\d]{4}$")
    private String pinCode;
}
