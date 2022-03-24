package ru.server.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import ru.server.dto.AccountDTO;
import ru.server.dto.BalanceDTO;
import ru.server.entity.Account;
import ru.server.entity.User;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegratedControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Создание пользователя в базе")
    public void createUser() {
        User user = getBaseTestUser();

        boolean isSaveUser = createUser(user);
        boolean isRemoveUser = removeUser(user);
        assertAll(
                () -> assertTrue(isRemoveUser),
                () -> assertTrue(isSaveUser)
        );
    }

    @Test
    @DisplayName("Сохранение счета в базе - УСПЕХ")
    void createScoreSuccess() {
        Account account = getBaseTestAccount();

        boolean isSaveUser = createUser(account.getUser());
        boolean isSaveAccount = createAccount(account);
        boolean isRemoveAccount = removeAccount(account);
        boolean isRemoveUser = removeUser(account.getUser());
        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertTrue(isSaveAccount),
                () -> assertTrue(isRemoveAccount),
                () -> assertTrue(isRemoveUser)
        );
    }

    @Test
    @DisplayName("Сохранение счета в базе - ПРОВАЛ (юзер не найден)")
    void createScoreFailure() {
        final long notExistUserId = Long.MAX_VALUE;
        Account account = getBaseTestAccount();
        account.getUser().setId(notExistUserId);

        User user = getBaseTestUser();

        boolean isSaveUser = createUser(user);
        boolean isSaveAccount = createAccount(account);
        boolean isRemoveUser = removeUser(account.getUser());
        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertFalse(isSaveAccount),
                () -> assertTrue(isRemoveUser)
        );
    }

    @Test
    @DisplayName("Получение баланса - УСПЕХ")
    void getBalanceSuccess() {
        //save new User and Account
        Account account = getBaseTestAccount();
        String expectedCardNumber = account.getCardNumber();
        String expectedAmount = account.getAmount().toString();

        boolean isSaveUser = createUser(account.getUser());
        boolean isSaveAccount = createAccount(account);
        //get balance of new Account
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCardNumber(account.getCardNumber());
        accountDTO.setPinCode(account.getPinCode());

        BalanceDTO balanceDTO = getBalance(accountDTO);

        boolean isRemoveAccount = removeAccount(account);
        boolean isRemoveUser = removeUser(account.getUser());
        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertTrue(isSaveAccount),
                () -> assertTrue(isRemoveAccount),
                () -> assertTrue(isRemoveUser),
                () -> assertEquals(expectedCardNumber, balanceDTO.getCardNumber()),
                () -> assertEquals(expectedAmount, balanceDTO.getAmount().toString())
        );
    }

    @Test
    @DisplayName("Получение баланса - ПРОВАЛ (неверный пин-код)")
    void getBalanceFailure() {
        //save new User and Account
        Account account = getBaseTestAccount();

        boolean isSaveUser = createUser(account.getUser());
        boolean isSaveAccount = createAccount(account);
        //prepare data to POST
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCardNumber(account.getCardNumber());
        //set WRONG-PIN
        accountDTO.setPinCode("9000");

        BalanceDTO balanceDTO = getBalance(accountDTO);

        boolean isRemoveAccount = removeAccount(account);
        boolean isRemoveUser = removeUser(account.getUser());
        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertTrue(isSaveAccount),
                () -> assertTrue(isRemoveAccount),
                () -> assertTrue(isRemoveUser),
                () -> assertNull(balanceDTO.getCardNumber()),
                () -> assertNull(balanceDTO.getAmount())
        );
    }

    private BalanceDTO getBalance(AccountDTO accountDTO) {
        final String getBalanceURL = "/host/balance";
        HttpEntity<AccountDTO> requestBalance = new HttpEntity<>(accountDTO);
        ResponseEntity<BalanceDTO> responseBalance = restTemplate.postForEntity(getBalanceURL, requestBalance, BalanceDTO.class);
        return responseBalance.getBody();
    }

    private boolean createUser(User user) {
        final String createUserURL = "/host/create/user";
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<String> response = restTemplate.postForEntity(createUserURL, request, String.class);

        return response.getBody().contains("USER SAVED");
    }

    private boolean createAccount(Account account) {
        final String createAccountURL = "/host/create/account";
        HttpEntity<Account> request = new HttpEntity<>(account);
        ResponseEntity<String> response = restTemplate.postForEntity(createAccountURL, request, String.class);

        return response.getBody().contains("ACCOUNT SAVED");
    }

    private boolean removeUser(User user) {
        final String removeUserURL = "/host/remove/user";
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<String> response = restTemplate.postForEntity(removeUserURL, request, String.class);

        return response.getBody().contains("removed");
    }

    private boolean removeAccount(Account account) {
        final String removeAccURL = "/host/remove/account";
        HttpEntity<Account> request = new HttpEntity<>(account);
        ResponseEntity<String> response = restTemplate.postForEntity(removeAccURL, request, String.class);

        return response.getBody().contains("removed");
    }

    private User getBaseTestUser() {
        User user = new User();
        user.setFirstName("Vlad");
        user.setLastName("Bodik");

        return user;
    }

    private Account getBaseTestAccount() {
        User user = getBaseTestUser();

        Account account = new Account();
        account.setUser(user);
        account.setAmount(new BigDecimal("9999.01"));
        account.setCardNumber("1234");
        account.setScoreNumber("4321");
        account.setPinCode("1111");

        return account;
    }
}
