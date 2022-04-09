package ru.client.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.client.dto.BalanceDTO;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnitATMServiceTest {

    @Autowired
    private ATMService atmService;

    private ResponseEntity<BalanceDTO> responseEntity;

    @Test
    @DisplayName("SHOW BALANCE - success")
    void showBalanceSuccess() {
        BalanceDTO balanceDTO = getBalanceDTO();

        responseEntity = new ResponseEntity<>(balanceDTO,HttpStatus.OK);
        String response = atmService.printBalanceResponse(responseEntity);

        assertTrue(response.contains("BALANCE : " + balanceDTO.getAmount()));
    }
    @Test
    @DisplayName("SHOW BALANCE - Invalid input data")
    void showBalanceInvalidInputData() {
        BalanceDTO balanceDTO = getBalanceDTO();
        balanceDTO.setStatus(HttpStatus.BAD_REQUEST);

        responseEntity = new ResponseEntity<>(balanceDTO,HttpStatus.BAD_REQUEST);
        String response = atmService.printBalanceResponse(responseEntity);

        assertTrue(response.contains("Invalid input data : check card_number and pin_code!"));
    }

    @Test
    @DisplayName("SHOW BALANCE - failure - wrong pin-code")
    void showBalanceWrongPin() {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setStatus(HttpStatus.EXPECTATION_FAILED);

        responseEntity = new ResponseEntity<>(balanceDTO,HttpStatus.EXPECTATION_FAILED);
        String response = atmService.printBalanceResponse(responseEntity);

        assertTrue(response.contains("WRONG PIN-CODE"));
    }

    @Test
    @DisplayName("SHOW BALANCE - failure - balance status == null")
    void showBalanceFailureNull() {
        BalanceDTO balanceDTO = new BalanceDTO();

        responseEntity = new ResponseEntity<>(balanceDTO,HttpStatus.OK);
        String response = atmService.printBalanceResponse(responseEntity);

        assertTrue(response.contains("Balance status is null"));
    }
    @Test
    @DisplayName("SHOW BALANCE - failure - unexpected status")
    void showBalanceFailureUnexpectedStatus() {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setStatus(HttpStatus.CHECKPOINT); // unknown status for ATMService

        responseEntity = new ResponseEntity<>(balanceDTO,HttpStatus.CHECKPOINT);
        String response = atmService.printBalanceResponse(responseEntity);

        assertTrue(response.contains("Unexpected HttpResponse status!!!"));
    }

    private BalanceDTO getBalanceDTO() {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setCardNumber("1111");
        balanceDTO.setAmount(new BigDecimal("999.5"));
        balanceDTO.setStatus(HttpStatus.OK);
        return balanceDTO;
    }

    @Test
    @DisplayName("MONEY TRANSACTION - success")
    void showResultOfTransactionSuccess(){
        BalanceDTO balanceBefore = new BalanceDTO("1",new BigDecimal("1000"),HttpStatus.OK);
        BalanceDTO balanceAfter = new BalanceDTO("1",new BigDecimal("0"),HttpStatus.OK);

        ResponseEntity<BalanceDTO> balanceBeforeTransfer = new ResponseEntity<>(balanceBefore,HttpStatus.OK);
        ResponseEntity<BalanceDTO> balanceAfterTransfer = new ResponseEntity<>(balanceAfter,HttpStatus.OK);
        BigDecimal amountToTransfer = new BigDecimal("1000");

        String printedMessage = atmService.printResultOfTransaction(balanceBeforeTransfer, balanceAfterTransfer, amountToTransfer);

        assertTrue(printedMessage.contains("Transfer success!"));
    }

    @Test
    @DisplayName("MONEY TRANSACTION - failure")
    void showResultOfTransactionFailure(){
        BalanceDTO balanceBefore = new BalanceDTO("1",new BigDecimal("1000"),HttpStatus.OK);
        BalanceDTO balanceAfter = new BalanceDTO("1",new BigDecimal("1000"),HttpStatus.OK);

        ResponseEntity<BalanceDTO> balanceBeforeTransfer = new ResponseEntity<>(balanceBefore,HttpStatus.OK);
        ResponseEntity<BalanceDTO> balanceAfterTransfer = new ResponseEntity<>(balanceAfter,HttpStatus.OK);
        BigDecimal amountToTransfer = new BigDecimal("100");

        String printedMessage = atmService.printResultOfTransaction(balanceBeforeTransfer, balanceAfterTransfer, amountToTransfer);

        assertTrue(printedMessage.contains("Transfer denied!"));
    }
}
