package ru.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import ru.client.dto.AccountDTO;
import ru.client.dto.BalanceDTO;

import java.io.StringWriter;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestWithAutowired {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void check() {
        AccountDTO accountDTO = getAccountDTO();
        BalanceDTO balanceDTO = getBalanceDTO();

        ResponseEntity balanceResponse = new ResponseEntity<>(balanceDTO, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenReturn(balanceResponse);

        ResponseEntity re = testRestTemplate.postForEntity("/client/balance", new HttpEntity<>(accountDTO), String.class);
        System.out.println();
    }

    @Test
    void checkMvc() throws Exception {
        // init
        AccountDTO accountDTO = getAccountDTO();
        BalanceDTO balanceDTO = getBalanceDTO();
        StringWriter sw = new StringWriter();
        // mock
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenReturn(new ResponseEntity(balanceDTO, HttpStatus.OK));
        // send request
        mockMvc.perform(
                        post("/client/balance", accountDTO)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(accountDTO))
                )
                .andDo(print(sw));
        // analyse results
        String response = sw.toString();
        assertTrue(response.contains(
                "CARD_NUMBER : " + balanceDTO.getCardNumber() + "\n" + "BALANCE : " + balanceDTO.getAmount()
        ));

    }

    private BalanceDTO getBalanceDTO() {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setCardNumber("1111");
        balanceDTO.setAmount(new BigDecimal("100.5"));
        return balanceDTO;
    }

    private AccountDTO getAccountDTO() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCardNumber("1111");
        accountDTO.setPinCode("0000");
        return accountDTO;
    }


}
