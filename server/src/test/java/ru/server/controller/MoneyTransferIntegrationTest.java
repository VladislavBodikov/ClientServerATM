package ru.server.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import ru.server.model.dto.BalanceDTO;
import ru.server.model.dto.TransactionDTO;
import ru.server.model.entity.Account;
import ru.server.model.entity.User;
import ru.server.model.Role;
import ru.server.repository.AccountCrudRepository;
import ru.server.repository.UserCrudRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static ru.server.DataForUnitTests.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoneyTransferIntegrationTest {

    @Autowired
    private AccountCrudRepository accountRepository;
    @Autowired
    private UserCrudRepository userRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    private String TRANSFER_URL = "/host/money/transfer";

    @AfterEach
    public void clearWasteDataAfterTests(){
        clearRepos(userRepository,accountRepository);
    }

    @Test
    @DisplayName("TRANSFER - success")
    void moneyTransferSuccess() {
        // prepare data in db
        Account accountFrom = getAccountFrom();
        Account accountTo = getAccountTo();
        User user = accountFrom.getUser();

        boolean isSavedUser = createUser(user, restTemplate, Role.ADMIN);
        boolean isSavedAccountFrom = createAccount(accountFrom, restTemplate, Role.ADMIN);
        boolean isSavedAccountTo = createAccount(accountTo, restTemplate, Role.ADMIN);

        TransactionDTO transactionRequestFrom = getTransactionDTO(accountFrom, accountTo.getCardNumber(), "100");
        ResponseEntity<BalanceDTO> responseBalanceOfAccountFrom = restTemplate
                .withBasicAuth(USERNAME_READ, PASSWORD_READ)
                .postForEntity(TRANSFER_URL, new HttpEntity<>(transactionRequestFrom), BalanceDTO.class);

        ResponseEntity<BalanceDTO> responseBalanceOfAccountTo = restTemplate
                .withBasicAuth(USERNAME_READ, PASSWORD_READ)
                .postForEntity("/host/balance", new HttpEntity<>(getAccountDTOFromAccount(accountTo)), BalanceDTO.class);

        assertAll(
                () -> assertEquals(responseBalanceOfAccountFrom.getBody().getAmount(), new BigDecimal("900.00")),
                () -> assertEquals(responseBalanceOfAccountTo.getBody().getAmount(), new BigDecimal("130.00"))
        );
    }

    @Test
    @DisplayName("TRANSFER - failure (card number to transfer not found)")
    void moneyTransferAccountToNotFoundFailure() {
        // prepare data in db
        Account accountFrom = getAccountFrom();
        Account accountTo = getAccountTo();
        User user = accountFrom.getUser();

        boolean isSavedUser = createUser(user, restTemplate, Role.ADMIN);
        boolean isSavedAccountFrom = createAccount(accountFrom, restTemplate, Role.ADMIN);

        TransactionDTO transactionRequest = getTransactionDTO(accountFrom, accountTo.getCardNumber(), "100");
        ResponseEntity<BalanceDTO> responseBalanceOfAccountFrom = restTemplate
                .withBasicAuth(USERNAME_READ, PASSWORD_READ)
                .postForEntity(TRANSFER_URL, new HttpEntity<>(transactionRequest), BalanceDTO.class);

        assertTrue(responseBalanceOfAccountFrom.getBody().getMessage().contains("Account with card_number"));
    }
    @Test
    @DisplayName("TRANSFER - failure (don`t have enough money for transfer)")
    void moneyTransferDontEnoughMoneyFailure() {
        // prepare data in db
        Account accountFrom = getAccountFrom();
        accountFrom.setAmount(new BigDecimal("0.0"));
        Account accountTo = getAccountTo();
        User user = accountFrom.getUser();

        boolean isSavedUser = createUser(user, restTemplate, Role.ADMIN);
        boolean isSavedAccountFrom = createAccount(accountFrom, restTemplate, Role.ADMIN);
        boolean isSavedAccountTo = createAccount(accountTo, restTemplate, Role.ADMIN);

        TransactionDTO transactionRequestFrom = getTransactionDTO(accountFrom, accountTo.getCardNumber(), "100");
        ResponseEntity<BalanceDTO> responseBalanceOfAccountFrom = restTemplate
                .withBasicAuth(USERNAME_READ, PASSWORD_READ)
                .postForEntity(TRANSFER_URL, new HttpEntity<>(transactionRequestFrom), BalanceDTO.class);

        assertTrue(responseBalanceOfAccountFrom.getBody().getMessage().contains("Don`t have enough money to transfer"));
    }
//
//    @Test
//    @DisplayName("TRANSFER - failure (tried to transfer to self card)")
//    void moneyTransferFailure() {
//        // prepare data in db
//        Account accountFrom = getAccountFrom();
//        Account accountTo = getAccountTo();
//        User user = accountFrom.getUser();
//
//        boolean isSavedUser = createUser(user, restTemplate, Role.ADMIN);
//        boolean isSavedAccountFrom = createAccount(accountFrom, restTemplate, Role.ADMIN);
//        boolean isSavedAccountTo = createAccount(accountTo, restTemplate, Role.ADMIN);
//
//        TransactionDTO transactionRequestFrom = getTransactionDTO(accountFrom, accountTo.getCardNumber(), "100");
//        ResponseEntity<BalanceDTO> responseBalanceOfAccountFrom = restTemplate
//                .withBasicAuth(USERNAME_READ, PASSWORD_READ)
//                .postForEntity(TRANSFER_URL, new HttpEntity<>(transactionRequestFrom), BalanceDTO.class);
//
//        ResponseEntity<BalanceDTO> responseBalanceOfAccountTo = restTemplate
//                .withBasicAuth(USERNAME_READ, PASSWORD_READ)
//                .postForEntity("/host/balance", new HttpEntity<>(getAccountDTOFromAccount(accountTo)), BalanceDTO.class);
//
//        assertTrue(responseBalanceOfAccountFrom.getBody().getMessage().contains("\\nAmount to transfer less or equals 0\\n\" + \"Amount: "));
//    }
//    @Test
//    @DisplayName("TRANSFER - failure (tried to transfer to self card)")
//    void moneyTransferFailure() {
//        // prepare data in db
//        Account accountFrom = getAccountFrom();
//        Account accountTo = getAccountTo();
//        User user = accountFrom.getUser();
//
//        boolean isSavedUser = createUser(user, restTemplate, Role.ADMIN);
//        boolean isSavedAccountFrom = createAccount(accountFrom, restTemplate, Role.ADMIN);
//        boolean isSavedAccountTo = createAccount(accountTo, restTemplate, Role.ADMIN);
//
//        TransactionDTO transactionRequestFrom = getTransactionDTO(accountFrom, accountTo.getCardNumber(), "100");
//        ResponseEntity<BalanceDTO> responseBalanceOfAccountFrom = restTemplate
//                .withBasicAuth(USERNAME_READ, PASSWORD_READ)
//                .postForEntity(TRANSFER_URL, new HttpEntity<>(transactionRequestFrom), BalanceDTO.class);
//
//        ResponseEntity<BalanceDTO> responseBalanceOfAccountTo = restTemplate
//                .withBasicAuth(USERNAME_READ, PASSWORD_READ)
//                .postForEntity("/host/balance", new HttpEntity<>(getAccountDTOFromAccount(accountTo)), BalanceDTO.class);
//
//        assertTrue(responseBalanceOfAccountFrom.getBody().getMessage().contains("\\nAmount to transfer less or equals 0\\n\" + \"Amount: "));
//    }

    private Account getAccountFrom() {
        Account account = getAccountWithoutId();
        account.setAccountNumber("40800000000000000008");
        account.setCardNumber("2222222222222222");
        account.setPinCode("2222");
        account.setAmount(new BigDecimal("1000"));
        return account;
    }

    private Account getAccountTo() {
        Account account = getAccountWithoutId();
        account.setAccountNumber("40800000000000000009");
        account.setCardNumber("3333333333333333");
        account.setPinCode("3333");
        account.setAmount(new BigDecimal("30"));
        return account;
    }

    private TransactionDTO getTransactionDTO(Account accountFrom, String cardNumberTo, String amountToTransfer) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountFrom(getAccountDTOFromAccount(accountFrom));
        transactionDTO.setCardNumberTo(cardNumberTo);
        transactionDTO.setAmountToTransfer(new BigDecimal(amountToTransfer));
        return transactionDTO;
    }
}
