package ru.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.client.controller.ATMRestController;
import ru.client.service.ATMService;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = Application.class)
public class ApplicationTest {

    @Autowired
    private ATMService atmService;
    @Autowired
    private ATMRestController atmRestController;

    @Test
    @DisplayName("Контекст загружается")
    public void contextLoad() {
        assertAll(
                () -> assertNotNull(atmService),
                () -> assertNotNull(atmRestController));
    }
}