package ru.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.server.controller.MainServerRestController;
import ru.server.repository.AccountCrudRepository;
import ru.server.repository.UserCrudRepository;
import ru.server.service.AccountService;
import ru.server.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApplicationTest {
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountCrudRepository accountCrudRepository;
    @Autowired
    private UserCrudRepository userCrudRepository;
    @Autowired
    private MainServerRestController mainServerRestController;

    @Test
    @DisplayName("Context load")
    public void contextLoad() {
        assertAll(
                () -> assertNotNull(userService),
                () -> assertNotNull(accountService),
                () -> assertNotNull(accountCrudRepository),
                () -> assertNotNull(userCrudRepository),
                () -> assertNotNull(mainServerRestController));
    }
}
