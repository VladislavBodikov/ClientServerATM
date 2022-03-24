package ru.client.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.client.Application;
import ru.client.dto.BalanceDTO;

import java.math.BigDecimal;

@SpringBootTest(classes = Application.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnitATMServiceTest {

    @Autowired
    private ATMService atmService;

    @Test
    @DisplayName("SHOW BALANCE - success")
    void showBalanceSuccess() {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setCardNumber("1111");
        balanceDTO.setAmount(new BigDecimal("999.5"));

        String response = atmService.showBalance(balanceDTO);

        assertTrue(response.contains("BALANCE : " + balanceDTO.getAmount()));
    }
    @Test
    @DisplayName("SHOW BALANCE - failure")
    void showBalanceFailure() {
        BalanceDTO balanceDTO = new BalanceDTO();

        String response = atmService.showBalance(balanceDTO);

        assertTrue(response.contains("WRONG PIN-CODE"));
    }
}
