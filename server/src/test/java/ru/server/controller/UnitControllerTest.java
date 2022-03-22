package ru.server.controller;

import static org.junit.jupiter.api.Assertions.*;
import static ru.server.DataForUnitTests.getAccount;
import static ru.server.DataForUnitTests.getUser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import ru.server.DataForUnitTests;
import ru.server.dto.AccountDTO;
import ru.server.dto.BalanceDTO;
import ru.server.entity.Account;
import ru.server.entity.User;
import ru.server.service.AccountService;
import ru.server.service.UserService;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Task: check HostRestController mapping to URL`s
 * available URL`s:
 *      1) /host/balance
 *
 *      2) /host/create/user
 *      3) /host/remove/user
 *
 *      4) /host/create/account
 *      5) /host/remove/account
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnitControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private AccountService accountService;
    @Autowired
    private TestRestTemplate restTemplate;
    /**
     * In this section check controller mapping to URLs:
     *      1) /host/balance
     */
    @Test
    @DisplayName("GET BALANCE - success")
    void getBalanceSuccessTest(){
        // 1. save User
        User user = getUser();
        Mockito.when(userService.save(Mockito.any())).thenReturn(Optional.of(user));
        boolean isUserSaved = createUser(user);
        // 2. save Account
        Account account = getAccount();
        Mockito.when(accountService.save(Mockito.any())).thenReturn(Optional.of(account));
        Mockito.when(userService.findByNameIfNotHaveId(Mockito.any())).thenReturn(Optional.of(user));
        boolean isAccountSaved = createAccount(account);

        // 3.CHECK BALANCE
        AccountDTO accountDTO = new AccountDTO();
        Mockito.when(accountService.findByCardNumber(account.getCardNumber())).thenReturn(Optional.of(account));
        accountDTO.setCardNumber(account.getCardNumber()); // cardNumber = 1234
        accountDTO.setPinCode(account.getPinCode());       // pinCode = 1111
        BalanceDTO balanceDTO = getBalance(accountDTO);
        boolean isGetBalance = new BigDecimal("9999.01").equals(balanceDTO.getAmount())
                                && "1234".equals(balanceDTO.getCardNumber());

        // 4. remove Account
        Mockito.when(accountService.removeByScoreNumber(Mockito.any())).thenReturn(1);
        boolean isAccountRemoved = removeAccount(account);
        // 5. remove User
        Mockito.when(userService.removeByFirstNameAndLastName(Mockito.any())).thenReturn(1);
        boolean isUserRemoved = removeUser(user);

        assertAll(
                ()->assertTrue(isUserSaved),
                ()->assertTrue(isAccountSaved),
                ()->assertTrue(isGetBalance),
                ()->assertTrue(isAccountRemoved),
                ()->assertTrue(isUserRemoved)
        );
    }
    @Test
    @DisplayName("GET BALANCE - failure (wrong-pin-code)")
    void getBalanceFailureTest(){
        // 1. save User
        User user = getUser();
        Mockito.when(userService.save(Mockito.any())).thenReturn(Optional.of(user));
        boolean isUserSaved = createUser(user);
        // 2. save Account
        Account account = getAccount();
        Mockito.when(accountService.save(Mockito.any())).thenReturn(Optional.of(account));
        Mockito.when(userService.findByNameIfNotHaveId(Mockito.any())).thenReturn(Optional.of(user));
        boolean isAccountSaved = createAccount(account);

        // 3.CHECK BALANCE
        AccountDTO accountDTO = new AccountDTO();
        Mockito.when(accountService.findByCardNumber(account.getCardNumber())).thenReturn(Optional.of(account));
        accountDTO.setCardNumber(account.getCardNumber());  // cardNumber = 1234
        accountDTO.setPinCode("some wrong pin-code");       // pinCode = 1111
        BalanceDTO balanceDTO = getBalance(accountDTO);
        boolean isBalanceNotFound = balanceDTO.getAmount() == null
                                 || balanceDTO.getCardNumber() == null;

        // 4. remove Account
        Mockito.when(accountService.removeByScoreNumber(Mockito.any())).thenReturn(1);
        boolean isAccountRemoved = removeAccount(account);
        // 5. remove User
        Mockito.when(userService.removeByFirstNameAndLastName(Mockito.any())).thenReturn(1);
        boolean isUserRemoved = removeUser(user);

        assertAll(
                ()->assertTrue(isUserSaved),
                ()->assertTrue(isAccountSaved),
                ()->assertTrue(isBalanceNotFound),
                ()->assertTrue(isAccountRemoved),
                ()->assertTrue(isUserRemoved)
        );
    }
    /**
     * In this section check controller mapping to URLs:
     *      2) /host/create/user
     *      3) /host/remove/user
     */
    @Test
    @DisplayName("USER - Create and remove")
    void createAndRemoveUserTest(){
        // 1. save User
        User user = getUser();
        Mockito.when(userService.save(Mockito.any())).thenReturn(Optional.of(user));
        boolean isSaved = createUser(user);

        // 2. remove User
        Mockito.when(userService.removeByFirstNameAndLastName(Mockito.any())).thenReturn(1);
        boolean isRemoved = removeUser(user);

        assertAll(
                ()->assertTrue(isSaved),
                ()->assertTrue(isRemoved)
                );
    }
    @Test
    @DisplayName("USER - Can`t create duplicate")
    void createDuplicateUserTest(){
        // 1. save User
        User user = getUser();
        Mockito.when(userService.save(Mockito.any())).thenReturn(Optional.of(user));
        boolean isSaved = createUser(user);
        // 2. try save duplicate user
        Mockito.when(userService.save(Mockito.any())).thenReturn(Optional.empty());
        boolean isSavedDuplicateUser = createUser(user);
        // 2. remove User
        Mockito.when(userService.removeByFirstNameAndLastName(Mockito.any())).thenReturn(1);
        boolean isRemoved = removeUser(user);

        assertAll(
                ()->assertTrue(isSaved),
                ()->assertFalse(isSavedDuplicateUser),
                ()->assertTrue(isRemoved)
        );
    }
    @Test
    @DisplayName("USER - Can`t remove User that not exist")
    void removeUserThatNotExistTest(){
        // 1. try to remove user that not exist in DB
        User user = getUser();
        Mockito.when(userService.removeByFirstNameAndLastName(Mockito.any())).thenReturn(0);
        boolean isRemoved = removeUser(user);

        assertFalse(isRemoved);
    }

    /**
     * In this section check controller mapping to URLs:
     *      4) /host/create/account
     *      5) /host/remove/account
     */
    @Test
    @DisplayName("ACCOUNT - Create and remove ")
    void createAndRemoveAccount(){
        // 1. save User
        User user = getUser();
        Mockito.when(userService.save(Mockito.any())).thenReturn(Optional.of(user));
        boolean isUserSaved = createUser(user);
        // 2. save Account
        Account account = getAccount();
        Mockito.when(accountService.save(Mockito.any())).thenReturn(Optional.of(account));
        Mockito.when(userService.findByNameIfNotHaveId(Mockito.any())).thenReturn(Optional.of(user));
        boolean isAccountSaved = createAccount(account);
        // 3. remove Account
        Mockito.when(accountService.removeByScoreNumber(Mockito.any())).thenReturn(1);
        boolean isAccountRemoved = removeAccount(account);
        // 4. remove User
        Mockito.when(userService.removeByFirstNameAndLastName(Mockito.any())).thenReturn(1);
        boolean isUserRemoved = removeUser(user);

        assertAll(
                ()->assertTrue(isUserSaved),
                ()->assertTrue(isAccountSaved),
                ()->assertTrue(isAccountRemoved),
                ()->assertTrue(isUserRemoved)
        );
    }
    @Test
    @DisplayName("ACCOUNT - Can`t create duplicate") //by scoreNumber and cardNumber
    void createDuplicateAccount(){
        // 1. save User
        User user = getUser();
        Mockito.when(userService.save(Mockito.any())).thenReturn(Optional.of(user));
        boolean isUserSaved = createUser(user);
        // 2. save Account
        Account account = getAccount();
        Mockito.when(accountService.save(Mockito.any())).thenReturn(Optional.of(account));
        Mockito.when(userService.findByNameIfNotHaveId(Mockito.any())).thenReturn(Optional.of(user));
        boolean isAccountSaved = createAccount(account);
        // 3. CREATE DUPLICATE ACCOUNT
        Mockito.when(accountService.save(Mockito.any())).thenReturn(Optional.empty());
        boolean isDuplicateSaved = createAccount(account);

        // 4. remove Account
        Mockito.when(accountService.removeByScoreNumber(Mockito.any())).thenReturn(1);
        boolean isAccountRemoved = removeAccount(account);
        // 5. remove User
        Mockito.when(userService.removeByFirstNameAndLastName(Mockito.any())).thenReturn(1);
        boolean isUserRemoved = removeUser(user);

        assertAll(
                ()->assertTrue(isUserSaved),
                ()->assertTrue(isAccountSaved),
                ()->assertFalse(isDuplicateSaved),
                ()->assertTrue(isAccountRemoved),
                ()->assertTrue(isUserRemoved)
        );
    }
    @Test
    @DisplayName("ACCOUNT - Cant remove account that not exist ")
    void removeAccountThatNotExist(){
        // 1. save User
        User user = getUser();
        Mockito.when(userService.save(Mockito.any())).thenReturn(Optional.of(user));
        boolean isUserSaved = createUser(user);
        // 2. save Account
        Account account = getAccount();
        Mockito.when(accountService.save(Mockito.any())).thenReturn(Optional.of(account));
        Mockito.when(userService.findByNameIfNotHaveId(Mockito.any())).thenReturn(Optional.of(user));
        boolean isAccountSaved = createAccount(account);
        // 3. remove Account
        Mockito.when(accountService.removeByScoreNumber(Mockito.any())).thenReturn(1);
        boolean isAccountRemoved = removeAccount(account);
        // 4. tru to remove account that not exist
        Mockito.when(accountService.removeByScoreNumber(Mockito.any())).thenReturn(0);
        boolean isRemoveAccountThatNotExist = removeAccount(account);
        // 4. remove User
        Mockito.when(userService.removeByFirstNameAndLastName(Mockito.any())).thenReturn(1);
        boolean isUserRemoved = removeUser(user);

        assertAll(
                ()->assertTrue(isUserSaved),
                ()->assertTrue(isAccountSaved),
                ()->assertTrue(isAccountRemoved),
                ()->assertFalse(isRemoveAccountThatNotExist),
                ()->assertTrue(isUserRemoved)
        );
    }
    @Test
    @DisplayName("ACCOUNT - Cant create account if User not found")
    void createAccountIfUserNotFound(){
        // 2. save Account
        Account account = getAccount();
        Mockito.when(userService.findByNameIfNotHaveId(Mockito.any())).thenReturn(Optional.empty());
        boolean isAccountSaved = createAccount(account);

        assertFalse(isAccountSaved);
    }

    private BalanceDTO getBalance(AccountDTO accountDTO){
        String url = "/host/balance";
        HttpEntity<AccountDTO> requestBalance = new HttpEntity<>(accountDTO);
        ResponseEntity<BalanceDTO> responseBalance = restTemplate.postForEntity(url, requestBalance, BalanceDTO.class);
        return responseBalance.getBody();
    }

    private boolean createUser(User user){
        String url = "/host/create/user";
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<String> response = restTemplate.postForEntity(url,request,String.class);

        return response.getBody().contains("USER SAVED");
    }

    private boolean createAccount(Account account){
        String url = "/host/create/account";
        HttpEntity<Account> request = new HttpEntity<>(account);
        ResponseEntity<String> response = restTemplate.postForEntity(url,request,String.class);

        return response.getBody().contains("ACCOUNT SAVED");
    }

    private boolean removeUser(User user){
        String url = "/host/remove/user";
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<String> response = restTemplate.postForEntity(url,request,String.class);

        return response.getBody().contains("removed");
    }

    private boolean removeAccount(Account account){
        String url = "/host/remove/account";
        HttpEntity<Account> request = new HttpEntity<>(account);
        ResponseEntity<String> response = restTemplate.postForEntity(url,request,String.class);

        return response.getBody().contains("removed");
    }
}
