package ru.server.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static ru.server.DataForUnitTests.*;

import ru.server.entity.Account;
import ru.server.exeption.AccountNotFoundException;
import ru.server.exeption.DontHaveEnoughMoneyException;
import ru.server.repository.AccountCrudRepository;
import ru.server.repository.UserCrudRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnitAccountServiceTest {
    @MockBean
    private AccountCrudRepository accountCrudRepository;
    @MockBean
    private UserService userService;
    @Autowired
    private AccountService accountService;

    @Test
    @DisplayName("SAVE - success")
    void saveSuccess() {
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.findByCardNumber(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(accountCrudRepository.findByAccountNumber(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(accountCrudRepository.save(Mockito.any())).thenReturn(account);
        Mockito.when(userService.findByNameIfNotHaveId(Mockito.any())).thenReturn(Optional.of(account.getUser()));

        boolean isAccountSave = accountService.save(account).isPresent();

        assertTrue(isAccountSave);
    }

    @Test
    @DisplayName("SAVE - failure (user not exist)")
    void saveAccountUserNotExistFailure() {
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.findByCardNumber(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(accountCrudRepository.findByAccountNumber(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(userService.findByNameIfNotHaveId(Mockito.any())).thenReturn(Optional.empty());

        boolean isAccountSave = accountService.save(account).isPresent();

        assertFalse(isAccountSave);
    }

    @Test
    @DisplayName("SAVE - failure (card_number exist)")
    void saveFailureCard() {
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.findByCardNumber(Mockito.any())).thenReturn(Optional.of(account));
        Mockito.when(accountCrudRepository.findByAccountNumber(Mockito.any())).thenReturn(Optional.empty());

        boolean isAccountSave = accountService.save(account).isPresent();

        assertFalse(isAccountSave);
    }

    @Test
    @DisplayName("SAVE - failure (score_number exist)")
    void saveAccountFailure() {
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.findByCardNumber(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(accountCrudRepository.findByAccountNumber(Mockito.any())).thenReturn(Optional.of(account));

        boolean isAccountSave = accountService.save(account).isPresent();

        assertFalse(isAccountSave);
    }

    @Test
    @DisplayName("REMOVE - by card number - success")
    void removeByCardNumberSuccess() {
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.findByCardNumber(Mockito.any())).thenReturn(Optional.of(account));
        Mockito.when(accountCrudRepository.removeByCardNumber(Mockito.any())).thenReturn(1);
        boolean isAccountRemoved = accountService.removeByCardNumber(account.getCardNumber()) > 0;

        assertTrue(isAccountRemoved);
    }

    @Test
    @DisplayName("REMOVE - by card number - failure")
    void removeByCardNumberFailure() {
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.removeByCardNumber(Mockito.any())).thenReturn(0);
        boolean isAccountRemoved = accountService.removeByCardNumber(account.getCardNumber()) > 0;

        assertFalse(isAccountRemoved);
    }

    @Test
    @DisplayName("REMOVE - by score number - success")
    void removeByAccountNumberSuccess() {
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.findByAccountNumber(Mockito.any())).thenReturn(Optional.of(account));
        Mockito.when(accountCrudRepository.removeByAccountNumber(Mockito.any())).thenReturn(1);
        boolean isAccountRemoved = accountService.removeByAccountNumber(account.getAccountNumber()) > 0;

        assertTrue(isAccountRemoved);
    }

    @Test
    @DisplayName("REMOVE - by score number - failure")
    void removeByAccountNumberFailure() {
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.removeByAccountNumber(Mockito.any())).thenReturn(0);
        boolean isAccountRemoved = accountService.removeByAccountNumber(account.getAccountNumber()) > 0;

        assertFalse(isAccountRemoved);
    }

    @Test
    @DisplayName("REMOVE - by ID - success")
    void removeByIdSuccess() {
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.existsById(Mockito.any())).thenReturn(true);
        Mockito.when(accountCrudRepository.removeById(Mockito.any())).thenReturn(1);
        boolean isAccountRemoved = accountService.removeById(account.getId()) > 0;

        assertTrue(isAccountRemoved);
    }

    @Test
    @DisplayName("REMOVE - by ID - failure")
    void removeByIdFailure(){
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.existsById(Mockito.any())).thenReturn(false);
        Mockito.when(accountCrudRepository.removeById(Mockito.any())).thenReturn(0);
        boolean isAccountRemoved = accountService.removeById(account.getId()) > 0;

        assertFalse(isAccountRemoved);
    }

    @Test
    @DisplayName("GET ALL ACCOUNTS")
    void getAccounts() {
        Account account1 = getAccountWithId();
        account1.setId(56);
        Account account2 = getAccountWithId();
        account2.setId(90);

        Mockito.when(accountCrudRepository.findAll())
                .thenReturn(new ArrayList<Account>() {{
                    add(account1);
                    add(account2);
                }});
        List<Account> accounts = accountService.getAllAccounts();
        boolean isHasAccountWithId56And90 = accounts.contains(account1) && accounts.contains(account2);

        assertTrue(isHasAccountWithId56And90);

    }

    @Test
    @DisplayName("TRANSACTION - success")
    void transactionSuccess(){
        Account accountFrom = getAccountWithId();
        accountFrom.setCardNumber("1111");
        accountFrom.setAmount(new BigDecimal("1000"));

        Account accountTo = getAccountWithId();
        accountTo.setCardNumber("2222");
        accountTo.setAmount(new BigDecimal("0"));

        String cardFrom = accountFrom.getCardNumber();
        String cardTo   = accountTo.getCardNumber();
        BigDecimal amountToTransfer = new BigDecimal("250");
        String EXPECTED_ACCOUNT_FROM_AMOUNT_AFTER_TRANSACTION =
                accountFrom.getAmount().subtract(amountToTransfer).toString(); // 750

        Mockito.when(accountCrudRepository.findByCardNumber(cardFrom)).thenReturn(Optional.of(accountFrom));
        Mockito.when(accountCrudRepository.findByCardNumber(cardTo)).thenReturn(Optional.of(accountTo));

        Account accountFromAfterTransaction = accountService.transactionCardToCard(cardFrom,amountToTransfer,cardTo);

        assertEquals(EXPECTED_ACCOUNT_FROM_AMOUNT_AFTER_TRANSACTION,accountFromAfterTransaction.getAmount().toString());
    }

    @Test
    @DisplayName("TRANSACTION - failure (don`t have enough money)")
    void transactionDontEnoughMoneyFailure(){
        Account accountFrom = getAccountWithId();
        accountFrom.setCardNumber("1111");
        accountFrom.setAmount(new BigDecimal("10"));

        Account accountTo = getAccountWithId();
        accountTo.setCardNumber("2222");

        String cardFrom = accountFrom.getCardNumber();
        String cardTo   = accountTo.getCardNumber();
        BigDecimal amountToTransfer = new BigDecimal("2500");

        Mockito.when(accountCrudRepository.findByCardNumber(cardFrom)).thenReturn(Optional.of(accountFrom));
        Mockito.when(accountCrudRepository.findByCardNumber(cardTo)).thenReturn(Optional.of(accountTo));


        assertThrows(DontHaveEnoughMoneyException.class,()->accountService.transactionCardToCard(cardFrom,amountToTransfer,cardTo));
    }

    @Test
    @DisplayName("TRANSACTION - failure (account not found)")
    void transactionAccountNotFoundFailure(){
        Account accountFrom = getAccountWithId();
        accountFrom.setCardNumber("1111");
        accountFrom.setAmount(new BigDecimal("10"));

        Account accountTo = getAccountWithId();
        accountTo.setCardNumber("2222");

        String cardFrom = accountFrom.getCardNumber();
        String cardTo   = accountTo.getCardNumber();
        BigDecimal amountToTransfer = new BigDecimal("2500");

        Mockito.when(accountCrudRepository.findByCardNumber(cardFrom)).thenReturn(Optional.of(accountFrom));
        Mockito.when(accountCrudRepository.findByCardNumber(cardTo)).thenReturn(Optional.empty());


        assertThrows(AccountNotFoundException.class,()->accountService.transactionCardToCard(cardFrom,amountToTransfer,cardTo));
    }
}
