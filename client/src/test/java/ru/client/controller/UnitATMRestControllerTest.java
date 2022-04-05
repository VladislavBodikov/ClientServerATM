package ru.client.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.client.dto.TransactionDTO;
import ru.client.service.ATMService;
import ru.client.dto.AccountDTO;
import ru.client.dto.BalanceDTO;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UnitATMRestControllerTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ATMService atmService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @InjectMocks
    @Spy
    private ATMRestController atmRestController;

    @Test
    @DisplayName("CHECK BALANCE - success")
    void checkBalance() throws Exception {
        // 1. request
        AccountDTO accountDTO = getAccountDTO("1111222211112222", "1221");
        // 2. response from SERVER
        ResponseEntity response = ResponseEntity
                .ok()
                .body(getBalanceDTO());
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any(Class.class)))
                .thenReturn(response);
        Mockito.when(atmService.printBalanceResponse(Mockito.any())).thenCallRealMethod();

        String answer = atmRestController.balance(accountDTO);
        String expect = "\nCARD_NUMBER : "   + getBalanceDTO().getCardNumber() +
                        "\nBALANCE : "       + getBalanceDTO().getAmount();

        Assertions.assertEquals(expect,answer);
    }

    @Test
    @DisplayName("/balance - check controller mapping - success")
    void checkMappingBalance(){
        AccountDTO accountDTO = getAccountDTO("1123","111");

        ResponseEntity response = ResponseEntity
                .ok()
                .body(getBalanceDTO());
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any(Class.class)))
                .thenReturn(response);

        String responseStr = sendAccountDTO(accountDTO);
        Assertions.assertEquals("ERROR : Response.body == null",responseStr);
    }

    private String sendAccountDTO(AccountDTO accountRequest) {
        HttpEntity<AccountDTO> request = new HttpEntity<>(accountRequest);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/client/balance", request, String.class);
        return response.getBody();
    }

    private AccountDTO getAccountDTO(String cardNumber, String pinCode) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCardNumber(cardNumber);
        accountDTO.setPinCode(pinCode);
        return accountDTO;
    }

    private BalanceDTO getBalanceDTO() {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setCardNumber("1111222211112222");
        balanceDTO.setAmount(new BigDecimal("10.5"));
        balanceDTO.setStatus(HttpStatus.OK);
        return balanceDTO;
    }

    @Test
    @DisplayName("MONEY TRANSACTION - success")
    void sendMoneySuccess(){
        // prepare data to send money
        AccountDTO accountFrom = getAccountDTO("1111","0000");
        BigDecimal amountToTransfer = new BigDecimal("800.00");

        TransactionDTO transactionRequest = getTransactionDTO(accountFrom, amountToTransfer);

        BalanceDTO balanceBefore = getBalanceDTO();
        balanceBefore.setAmount(new BigDecimal("1000"));

        BalanceDTO balanceAfter  = getBalanceDTO();
        balanceAfter.setAmount(new BigDecimal("200"));

        // mock response from server
        ResponseEntity<BalanceDTO> responseBalanceBeforeTrans = ResponseEntity.ok().body(balanceBefore);
        ResponseEntity<BalanceDTO> responseBalanceAfterTrans = ResponseEntity.ok().body(balanceAfter);
        String EXPECTED_MESSAGE_AFTER_SUCCESS_TRANSACTION = "Transfer success!";

        HttpEntity<AccountDTO> request = new HttpEntity<>(accountFrom);
        Mockito.when(restTemplate.postForEntity("http://localhost:8082/host/balance", request, BalanceDTO.class))
                .thenReturn(responseBalanceBeforeTrans);
        Mockito.when(restTemplate.postForEntity("http://localhost:8082/host/money/transfer", transactionRequest, BalanceDTO.class))
                .thenReturn(responseBalanceAfterTrans);
        Mockito.when(atmService.printResultOfTransaction(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(EXPECTED_MESSAGE_AFTER_SUCCESS_TRANSACTION);
        // get answer
        String responseStr = atmRestController.sendMoney(transactionRequest);

        Assertions.assertEquals(EXPECTED_MESSAGE_AFTER_SUCCESS_TRANSACTION,responseStr);
    }

    private TransactionDTO getTransactionDTO(AccountDTO accountFrom, BigDecimal amountToTransfer) {
        TransactionDTO transactionRequest = new TransactionDTO();
        transactionRequest.setAccountFrom(accountFrom);
        transactionRequest.setCardNumberTo("2222");
        transactionRequest.setAmountToTransfer(amountToTransfer);
        return transactionRequest;
    }

    @Test
    @DisplayName("MONEY TRANSACTION - failure (don`t have enough money for transfer)")
    void sendMoneyDontHaveEnoughMoneyFailure(){
        // prepare data to send money
        AccountDTO accountFrom = getAccountDTO("1111","0000");
        BigDecimal amountToTransfer = new BigDecimal("800.00");

        TransactionDTO transactionRequest = getTransactionDTO(accountFrom, amountToTransfer);

        BalanceDTO balanceBefore = getBalanceDTO();
        balanceBefore.setAmount(new BigDecimal("300"));

        // mock response from server
        ResponseEntity<BalanceDTO> responseBalanceBeforeTrans = ResponseEntity.ok().body(balanceBefore);
        String EXPECTED_MESSAGE_AFTER_TRANSACTION = "\nTransaction denied! \nDon`t have enough money to transfer!\n";

        HttpEntity<AccountDTO> request = new HttpEntity<>(accountFrom);
        Mockito.when(restTemplate.postForEntity("http://localhost:8082/host/balance", request, BalanceDTO.class))
                .thenReturn(responseBalanceBeforeTrans);

        // get answer
        String responseStr = atmRestController.sendMoney(transactionRequest);

        Assertions.assertEquals(EXPECTED_MESSAGE_AFTER_TRANSACTION,responseStr);
    }

    @Test
    @DisplayName("MONEY TRANSACTION - failure (wrong pin)")
    void sendMoneyWrongPinCodeFailure(){
        // prepare data to send money
        AccountDTO accountFrom = getAccountDTO("1111","0000");
        BigDecimal amountToTransfer = new BigDecimal("800.00");

        TransactionDTO transactionRequest = getTransactionDTO(accountFrom, amountToTransfer);

        // mock response from server
        ResponseEntity<BalanceDTO> responseBalanceBeforeTrans = ResponseEntity.ok().body(null);
        String EXPECTED_MESSAGE_AFTER_TRANSACTION = "\nWRONG PIN-CODE\n";

        HttpEntity<AccountDTO> request = new HttpEntity<>(accountFrom);
        Mockito.when(restTemplate.postForEntity("http://localhost:8082/host/balance", request, BalanceDTO.class))
                .thenReturn(responseBalanceBeforeTrans);

        // get answer
        String responseStr = atmRestController.sendMoney(transactionRequest);

        Assertions.assertEquals(EXPECTED_MESSAGE_AFTER_TRANSACTION,responseStr);
    }

    @Test
    @DisplayName("MONEY TRANSACTION - failure (rest client exception - has not connection with server)")
    void sendMoneyRestClientExeptionFailure(){
        // prepare data to send money
        AccountDTO accountFrom = getAccountDTO("1111","0000");
        BigDecimal amountToTransfer = new BigDecimal("800.00");

        TransactionDTO transactionRequest = getTransactionDTO(accountFrom, amountToTransfer);

        BalanceDTO balanceBefore = getBalanceDTO();
        balanceBefore.setAmount(new BigDecimal("1000"));

        // mock response from server
        ResponseEntity<BalanceDTO> responseBalanceBeforeTrans = ResponseEntity.ok().body(balanceBefore);
        String EXPECTED_MESSAGE_AFTER_TRANSACTION = "\nTransfer denied!\n";

        HttpEntity<AccountDTO> request = new HttpEntity<>(accountFrom);
        HttpEntity<TransactionDTO> requestTransaction = new HttpEntity<>(transactionRequest);
        Mockito.when(restTemplate.postForEntity("http://localhost:8082/host/balance", request, BalanceDTO.class))
                .thenReturn(responseBalanceBeforeTrans);
        Mockito.when(restTemplate.postForEntity("http://localhost:8082/host/money/transfer", requestTransaction, BalanceDTO.class))
                .thenThrow(new RestClientException("test throw exception"));
        Mockito.when(atmService.printResultOfTransaction(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(EXPECTED_MESSAGE_AFTER_TRANSACTION);

        // get answer
        String responseStr = atmRestController.sendMoney(transactionRequest);

        Assertions.assertEquals(EXPECTED_MESSAGE_AFTER_TRANSACTION,responseStr);
    }

}
