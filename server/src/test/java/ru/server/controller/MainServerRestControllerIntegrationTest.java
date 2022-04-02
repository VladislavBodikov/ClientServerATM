package ru.server.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.server.dto.AccountDTO;
import ru.server.dto.BalanceDTO;
import ru.server.entity.Account;
import ru.server.entity.Role;
import ru.server.entity.User;
import ru.server.repository.AccountCrudRepository;
import ru.server.repository.UserCrudRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.server.DataForUnitTests.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainServerRestControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserCrudRepository userRepository;
    @Autowired
    private AccountCrudRepository accountRepository;

    private List<CrudRepository> repositories;

    private void clearRepositories() {
        if (repositories == null) {
            repositories = new ArrayList<>() {{
                add(accountRepository);
                add(userRepository);
            }};
        }
        clearRepos(repositories);
    }

    @Test
    @DisplayName("GET BALANCE - success")
    void getBalanceSuccess() {
        //save new User and Account
        Account account = getAccountWithoutId();
        String expectedCardNumber = account.getCardNumber();
        String expectedAmount = account.getAmount().toString();

        boolean isSaveUser = createUser(account.getUser(), restTemplate, Role.ADMIN);
        boolean isSaveAccount = createAccount(account, restTemplate, Role.ADMIN);
        //get balance of new Account
        AccountDTO accountDTO = getAccountDTO(account);
        BalanceDTO balanceDTO = getBalanceFromServerByAccountDTO(accountDTO);

        clearRepositories();

        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertTrue(isSaveAccount),
                () -> assertEquals(expectedCardNumber, balanceDTO.getCardNumber()),
                () -> assertEquals(expectedAmount, balanceDTO.getAmount().toString())
        );
    }

    private AccountDTO getAccountDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCardNumber(account.getCardNumber());
        accountDTO.setPinCode(account.getPinCode());
        return accountDTO;
    }

    private BalanceDTO getBalanceFromServerByAccountDTO(AccountDTO accountDTO) {
        String getBalanceURL = "/host/balance";

        HttpEntity<AccountDTO> requestBalance = new HttpEntity<>(accountDTO);
        ResponseEntity<BalanceDTO> responseBalance = restTemplate.withBasicAuth(USERNAME_READ, PASSWORD_READ).postForEntity(getBalanceURL, requestBalance, BalanceDTO.class);

        return responseBalance.getBody();
    }

    @Test
    @DisplayName("GET BALANCE - failure (Invalid input data in AccountDTO)")
    void getBalanceInvalidDataFailure() {
        //save new User and Account
        Account account = getAccountWithoutId();
        String NOT_VALID_PIN_CODE = "not-valid-pin-code";
        HttpStatus EXPECTED_STATUS = HttpStatus.BAD_REQUEST;

        boolean isSaveUser = createUser(account.getUser(), restTemplate, Role.ADMIN);
        boolean isSaveAccount = createAccount(account, restTemplate, Role.ADMIN);

        //get balance of new Account
        AccountDTO accountDTO = getAccountDTO(account);
        accountDTO.setPinCode(NOT_VALID_PIN_CODE);
        BalanceDTO balanceDTO = getBalanceFromServerByAccountDTO(accountDTO);

        clearRepositories();

        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertTrue(isSaveAccount),
                () -> assertEquals(EXPECTED_STATUS, balanceDTO.getStatus()),
                () -> assertNull(balanceDTO.getAmount()),
                () -> assertNull(balanceDTO.getCardNumber())
        );
    }

    @Test
    @DisplayName("GET BALANCE - failure (wrong pin-code)")
    void getBalanceWrongPinFailure() {
        //save new User and Account
        Account account = getAccountWithoutId();
        String RIGHT_PIN_CODE = account.getPinCode(); // 1111
        String WRONG_PIN_CODE = "9999";
        HttpStatus EXPECTED_STATUS = HttpStatus.EXPECTATION_FAILED;

        boolean isSaveUser = createUser(account.getUser(), restTemplate, Role.ADMIN);
        boolean isSaveAccount = createAccount(account, restTemplate, Role.ADMIN);

        //get balance of new Account
        AccountDTO accountDTO = getAccountDTO(account);
        accountDTO.setPinCode(WRONG_PIN_CODE);
        BalanceDTO balanceDTO = getBalanceFromServerByAccountDTO(accountDTO);

        clearRepositories();

        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertTrue(isSaveAccount),
                () -> assertEquals(EXPECTED_STATUS, balanceDTO.getStatus()),
                () -> assertNull(balanceDTO.getAmount()),
                () -> assertNull(balanceDTO.getCardNumber())
        );
    }

    @Test
    @DisplayName("GET ALL USERS")
    void getUsers() {
        User user = getUserWithoutId();
        boolean isSaveUser = createUser(user, restTemplate, Role.ADMIN);

        long savedUserId = userRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName()).get().getId();
        String EXPECTED_LIST_OF_USERS =
                String.format("User(id=%s, firstName=Vlad, lastName=Bodik, passportData=null, status=ACTIVE, role=USER)\r\n"
                        , savedUserId);

        ResponseEntity<String> response = restTemplate.withBasicAuth(USERNAME_READ, USERNAME_READ).getForEntity("/host/users", String.class);

        clearRepositories();

        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertEquals(EXPECTED_LIST_OF_USERS, response.getBody())
        );
    }

    @Test
    @DisplayName("GET ALL ACCOUNTS")
    void getAccounts() {
        User user = getUserWithoutId();
        Account account = getAccountWithoutId();

        boolean isSaveUser = createUser(user, restTemplate, Role.ADMIN);
        boolean isSaveAccount = createAccount(account, restTemplate, Role.ADMIN);

        long savedAccountId = accountRepository.findByAccountNumber(account.getAccountNumber()).get().getId();
        String EXPECTED_LIST_OF_ACCOUNTS =
                String.format("Account(id=%s, cardNumber=1111000011110000, scoreNumber=40804080408040804080, amount=9999.01, pinCode=****)\r\n"
                        , savedAccountId);


        ResponseEntity<String> response = restTemplate.withBasicAuth(USERNAME_READ, USERNAME_READ).getForEntity("/host/accounts", String.class);

        clearRepositories();

        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertTrue(isSaveAccount),
                () -> assertEquals(EXPECTED_LIST_OF_ACCOUNTS, response.getBody())
        );
    }

}
