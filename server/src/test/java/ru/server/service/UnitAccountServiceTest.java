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
import ru.server.repository.AccountCrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnitAccountServiceTest {
    @MockBean
    private AccountCrudRepository accountCrudRepository;
    @Autowired
    private AccountService accountService;

    @Test
    @DisplayName("SAVE - success")
    void saveSuccess(){
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.findByCardNumber(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(accountCrudRepository.findByScoreNumber(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(accountCrudRepository.save(Mockito.any())).thenReturn(account);

        boolean isAccountSave = accountService.save(account).isPresent();

        assertTrue(isAccountSave);
    }
    @Test
    @DisplayName("SAVE - failure (card_number exist)")
    void saveFailureCard(){
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.findByCardNumber(Mockito.any())).thenReturn(Optional.of(account));
        Mockito.when(accountCrudRepository.findByScoreNumber(Mockito.any())).thenReturn(Optional.empty());

        boolean isAccountSave = accountService.save(account).isPresent();

        assertFalse(isAccountSave);
    }
    @Test
    @DisplayName("SAVE - failure (score_number exist)")
    void saveFailureScore(){
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.findByCardNumber(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(accountCrudRepository.findByScoreNumber(Mockito.any())).thenReturn(Optional.of(account));

        boolean isAccountSave = accountService.save(account).isPresent();

        assertFalse(isAccountSave);
    }
    @Test
    @DisplayName("REMOVE - by card number - success")
    void removeByCardNumberSuccess(){
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.findByCardNumber(Mockito.any())).thenReturn(Optional.of(account));
        Mockito.when(accountCrudRepository.removeByCardNumber(Mockito.any())).thenReturn(1);
        boolean isAccountRemoved = accountService.removeByCardNumber(account.getCardNumber()) > 0;

        assertTrue(isAccountRemoved);
    }
    @Test
    @DisplayName("REMOVE - by card number - failure")
    void removeByCardNumberFailure(){
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.removeByCardNumber(Mockito.any())).thenReturn(0);
        boolean isAccountRemoved = accountService.removeByCardNumber(account.getCardNumber()) > 0;

        assertFalse(isAccountRemoved);
    }
    @Test
    @DisplayName("REMOVE - by score number - success")
    void removeByScoreNumberSuccess(){
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.findByScoreNumber(Mockito.any())).thenReturn(Optional.of(account));
        Mockito.when(accountCrudRepository.removeByScoreNumber(Mockito.any())).thenReturn(1);
        boolean isAccountRemoved = accountService.removeByScoreNumber(account.getScoreNumber()) > 0;

        assertTrue(isAccountRemoved);
    }
    @Test
    @DisplayName("REMOVE - by score number - failure")
    void removeByScoreNumberFailure(){
        Account account = getAccountWithId();
        Mockito.when(accountCrudRepository.removeByScoreNumber(Mockito.any())).thenReturn(0);
        boolean isAccountRemoved = accountService.removeByScoreNumber(account.getScoreNumber()) > 0;

        assertFalse(isAccountRemoved);
    }
    @Test
    @DisplayName("GET ALL ACCOUNTS")
    void getAccounts(){
        Account account1 = getAccountWithId();
        account1.setId(56);
        Account account2 = getAccountWithId();
        account2.setId(90);

        Mockito.when(accountCrudRepository.findAll())
                .thenReturn(new ArrayList<Account>(){{add(account1);add(account2);}});
        List<Account> accounts = accountService.getAllAccounts();
        boolean isHasAccountWithId56And90 = accounts.contains(account1) && accounts.contains(account2);

        assertTrue(isHasAccountWithId56And90);

    }
}
