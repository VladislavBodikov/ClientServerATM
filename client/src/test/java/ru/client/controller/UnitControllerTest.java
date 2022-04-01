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
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import ru.client.service.ATMService;
import ru.client.dto.AccountDTO;
import ru.client.dto.BalanceDTO;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UnitControllerTest {

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
    @DisplayName("CHECK balance - success")
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
    @DisplayName("/balance - check controller mapping ")
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


}
