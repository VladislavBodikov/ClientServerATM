package ru.client.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.client.dto.BalanceDTO;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnitATMServiceTest {

    @Autowired
    private ATMService atmService;

    @Test
    @DisplayName("SHOW BALANCE - success")
    void showBalanceSuccess() {
        BalanceDTO balanceDTO = getBalanceDTO();

        String response = atmService.showBalance(balanceDTO);

        assertTrue(response.contains("BALANCE : " + balanceDTO.getAmount()));
    }
    @Test
    @DisplayName("SHOW BALANCE - Invalid input data")
    void showBalanceInvalidInputData() {
        BalanceDTO balanceDTO = getBalanceDTO();
        balanceDTO.setStatus(HttpStatus.BAD_REQUEST);

        String response = atmService.showBalance(balanceDTO);

        assertTrue(response.contains("Invalid input data : check card_number and pin_code!"));
    }

    @Test
    @DisplayName("SHOW BALANCE - failure - wrong pin-code")
    void showBalanceWrongPin() {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setStatus(HttpStatus.EXPECTATION_FAILED);

        String response = atmService.showBalance(balanceDTO);

        assertTrue(response.contains("WRONG PIN-CODE"));
    }

    @Test
    @DisplayName("SHOW BALANCE - failure - unexpected status")
    void showBalanceFailure() {
        BalanceDTO balanceDTO = new BalanceDTO();

        String response = atmService.showBalance(balanceDTO);

        assertTrue(response.contains("Unexpected HttpResponse status!!!"));
    }

    private BalanceDTO getBalanceDTO() {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setCardNumber("1111");
        balanceDTO.setAmount(new BigDecimal("999.5"));
        balanceDTO.setStatus(HttpStatus.OK);
        return balanceDTO;
    }
}
