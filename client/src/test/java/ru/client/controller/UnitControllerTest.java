package ru.client.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.client.dto.AccountDTO;
import ru.client.dto.BalanceDTO;
import ru.client.service.ATMService;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnitControllerTest {

    @MockBean
    private ATMService atmService;
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private ResponseEntity<BalanceDTO> responseEntity;
    @Autowired
    private TestRestTemplate testRestTemplate;

    /**
     * In this section check controller mapping to URLs:
     *      1) /client/balance
     */
//    @Test
//    @DisplayName("CHECK balance - success")
//    void checkBalance(){
//        AccountDTO accountRequest = getAccountDTO("1111000011110000","1001");
//
//        BalanceDTO response = getBalanceDTO();
//        ResponseEntity<BalanceDTO> re = new ResponseEntity<>(response, HttpStatus.OK);
//        Mockito
//                .when(restTemplate.postForEntity("http://localhost:8082/host/balance",Mockito.any(),BalanceDTO.class))
//                .thenReturn(re);
//        Mockito.when(responseEntity.getBody()).thenReturn(response);
//
//
//        String answer = sendAccountDTO(accountRequest);
//
//        assertTrue(answer.contains("BALANCE : " + response.getAmount()));
//    }
    @Test
    @DisplayName("CHECK balance - success")
    void checkBalance(){
        AccountDTO accountRequest = getAccountDTO("11110000","1001");

        BalanceDTO response = getBalanceDTO();
//        ResponseEntity<BalanceDTO> re = new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        //ResponseEntity re = new ResponseEntity(response, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(new ResponseEntity(response,HttpStatus.OK));
//        Mockito.doReturn(re).when(restTemplate.postForEntity(Mockito.any(), Mockito.any(), Mockito.any()));
        ResponseEntity re = restTemplate.postForEntity("http://localhost:8082/host/balance",accountRequest,BalanceDTO.class);
        String answer = sendAccountDTO(accountRequest);

        assertTrue(answer.contains("BALANCE : " + response.getAmount()));
    }

    private String sendAccountDTO(AccountDTO accountRequest){
        HttpEntity<AccountDTO> request = new HttpEntity<>(accountRequest);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/client/balance",request,String.class);
        return response.getBody();
    }

    private AccountDTO getAccountDTO(String cardNumber, String pinCode){
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCardNumber(cardNumber);
        accountDTO.setPinCode(pinCode);
        return accountDTO;
    }
    private BalanceDTO getBalanceDTO(){
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setCardNumber("111");
        balanceDTO.setAmount(new BigDecimal("10.5"));
        return balanceDTO;
    }


}
