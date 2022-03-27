package ru.client.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.client.dto.AccountDTO;
import ru.client.dto.BalanceDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ResponseEntityTest {

    private final String SERVER_URL = "http://localhost:8082/host/balance";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void check() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCardNumber("1111222211112222");
        accountDTO.setPinCode("1221");
        ResponseEntity<BalanceDTO> response;
        BalanceDTO responseBalance;
        try {
            response = restTemplate.postForEntity(SERVER_URL, new HttpEntity<>(accountDTO),BalanceDTO.class);
            responseBalance = response.getBody();
            if (responseBalance.getStatus() == HttpStatus.BAD_REQUEST)
                System.out.println();
            if (responseBalance.getStatus() == HttpStatus.OK)
                System.out.println(responseBalance);
        } catch (RestClientException exception) {
            exception.printStackTrace();
        }
    }
}
