package ru.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import ru.client.controller.ATMRestController;

import ru.client.service.ATMService;
import ru.client.dto.BalanceDTO;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ApplicationTest {

    @Autowired
    private ATMService atmService;
    @Autowired
    private ATMRestController atmRestController;
    @Autowired
    private RestTemplate restTemplate;

    @Test
    @DisplayName("Контекст загружается")
    public void contextLoad() {
        atmService.showBalance(new BalanceDTO());
        assertAll(
                () -> assertNotNull(atmService),
                () -> assertNotNull(restTemplate),
                () -> assertNotNull(atmRestController));
    }
}
