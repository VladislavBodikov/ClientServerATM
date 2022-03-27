package ru.common.dto;

import org.junit.jupiter.api.Test;
import ru.common.dto.AccountDTO;
import ru.common.dto.BalanceDTO;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DtoTest {
    @Test
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
    void balanceTest(){
        BalanceDTO balanceNoArg = new BalanceDTO();
        balanceNoArg.setCardNumber("111");
        balanceNoArg.setAmount(new BigDecimal("100.5"));

        BalanceDTO balanceAllArgs = new BalanceDTO(balanceNoArg.getCardNumber(),balanceNoArg.getAmount());
        assertAll(
                ()->assertEquals("111",balanceNoArg.getCardNumber()),
                ()->assertEquals("100.5",balanceNoArg.getAmount().toString()),
                ()->assertEquals("111",balanceAllArgs.getCardNumber()),
                ()->assertEquals("100.5",balanceAllArgs.getAmount().toString())
        );
    }
}
