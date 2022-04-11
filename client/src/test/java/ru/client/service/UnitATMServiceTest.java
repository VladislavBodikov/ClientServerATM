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

        responseEntity = new ResponseEntity<>(balanceDTO, HttpStatus.OK);
        String response = atmService.printBalanceResponse(responseEntity);

        assertTrue(response.contains("BALANCE : " + balanceDTO.getAmount()));
    }

    @Test
    @DisplayName("SHOW BALANCE - failure (server response == null)")
    void showBalanceHasNotConnectionWithServer() {
        responseEntity = null;
        String response = atmService.printBalanceResponse(responseEntity);

        assertTrue(response.contains("ERROR : response == null. Check connection with server!"));
    }

    @Test
    @DisplayName("SHOW BALANCE - failure (server response body == null)")
    void showBalanceInvalidResponseBody() {
        responseEntity = new ResponseEntity<>(null, HttpStatus.CONFLICT);
        String response = atmService.printBalanceResponse(responseEntity);

        assertTrue(response.contains("ERROR : Response.body == null"));
    }

    @Test
    @DisplayName("SHOW BALANCE - failure (print message from server)")
    void showBalancePrintMessage() {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setMessage("some message from server");

        responseEntity = new ResponseEntity<>(balanceDTO, HttpStatus.CHECKPOINT);
        String response = atmService.printBalanceResponse(responseEntity);

        assertTrue(response.contains("some message from server"));
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
    void showResultOfTransactionSuccess() {
        BalanceDTO balanceBefore = new BalanceDTO("1", new BigDecimal("1000"), HttpStatus.OK, null);

        ResponseEntity<BalanceDTO> balanceBeforeTransfer = new ResponseEntity<>(balanceBefore, HttpStatus.OK);

        String printedMessage = atmService.printResultOfTransaction(balanceBeforeTransfer);

        assertTrue(printedMessage.contains("Transfer success!"));
    }
    @Test
    @DisplayName("MONEY TRANSACTION - failure (server response == null)")
    void showResultOfTransactionHasNotConnectionWithServer() {
        responseEntity = null;
        String response = atmService.printResultOfTransaction(responseEntity);

        assertTrue(response.contains("ERROR : response == null. Check connection with server!"));
    }
    @Test
    @DisplayName("MONEY TRANSACTION - failure (server response body == null)")
    void showResultOfTransactionInvalidResponseBody() {
        responseEntity = new ResponseEntity<>(null, HttpStatus.CONFLICT);
        String response = atmService.printResultOfTransaction(responseEntity);

        assertTrue(response.contains("ERROR : Response.body == null"));
    }

    @Test
    @DisplayName("MONEY TRANSACTION - failure")
    void showResultOfTransactionPrintMessage() {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setMessage("some message from server");

        responseEntity = new ResponseEntity<>(balanceDTO, HttpStatus.CHECKPOINT);
        String response = atmService.printResultOfTransaction(responseEntity);

        assertTrue(response.contains("some message from server"));
    }
}
