package ru.server.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class DtoTest {
    @Test
    @DisplayName("AccountDTO fields")
    void accountTest(){
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCardNumber("111");
        accountDTO.setPinCode("000");
        assertAll(
                ()->assertEquals("111",accountDTO.getCardNumber()),
                ()->assertEquals("000",accountDTO.getPinCode())
        );
    }
    @Test
    @DisplayName("BalanceDTO fields")
    void balanceTest(){
        BalanceDTO balanceNoArg = new BalanceDTO();
        balanceNoArg.setCardNumber("111");
        balanceNoArg.setAmount(new BigDecimal("100.5"));
        balanceNoArg.setStatus(HttpStatus.OK);

        assertAll(
                ()->assertEquals("111",balanceNoArg.getCardNumber()),
                ()->assertEquals("100.5",balanceNoArg.getAmount().toString()),
                ()->assertEquals(HttpStatus.OK,balanceNoArg.getStatus())
        );
    }
}
