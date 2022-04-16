package ru.server;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.server.model.dto.AccountDTO;
import ru.server.model.entity.Account;
import ru.server.model.Role;
import ru.server.model.entity.User;
import ru.server.repository.AccountCrudRepository;
import ru.server.repository.UserCrudRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataForUnitTests {

    //Basic auth to WRITE DATA
    public static String USERNAME_WRITE = "1111333311113333";
    public static String PASSWORD_WRITE = "1221";
    //Basic auth to READ DATA
    public static String USERNAME_READ = "1111222211112222";
    public static String PASSWORD_READ = "1221";


    public static User getUserWithId() {
        User user = new User();
        user.setId(1);
        user.setFirstName("Vlad");
        user.setLastName("Bodik");
        return user;
    }

    public static User getUserWithoutId() {
        User user = new User();
        user.setFirstName("Vlad");
        user.setLastName("Bodik");

        return user;
    }

    public static Account getAccountWithId() {
        User user = getUserWithId();

        Account account = new Account();
        account.setId(1);
        account.setUser(user);
        account.setAmount(new BigDecimal("9999.01"));
        account.setCardNumber("1111000011110000");
        account.setAccountNumber("40804080408040804080");
        account.setPinCode("1111");

        return account;
    }

    public static Account getAccountWithoutId() {
        User user = getUserWithoutId();

        Account account = new Account();
        account.setUser(user);
        account.setAmount(new BigDecimal("9999.01"));
        account.setCardNumber("1111000011110000");
        account.setAccountNumber("40804080408040804080");
        account.setPinCode("1111");

        return account;
    }

    public static void clearRepos(UserCrudRepository userRepository, AccountCrudRepository accountRepository) {
        int COUNT_OF_BASE_USER_IN_DB = 2;
        int COUNT_ACCOUNTS_OF_BASED_USERS_IN_DB = 2;

        if (userRepository != null){
            List<User> users = new ArrayList<>();
            userRepository.findAll().forEach(users::add);
            users.stream().skip(COUNT_OF_BASE_USER_IN_DB).forEach(userRepository::delete);
        }
        if (accountRepository != null){
            List<Account> accounts = new ArrayList<>();
            accountRepository.findAll().forEach(accounts::add);
            accounts.stream().skip(COUNT_ACCOUNTS_OF_BASED_USERS_IN_DB).forEach(accountRepository::delete);
        }
    }

    public static boolean createUser(User user, TestRestTemplate restTemplate, Role role) {
        String url = "/host/create/user";
        HttpEntity<User> request = new HttpEntity<>(user);

        ResponseEntity<String> response = getStringResponseEntityByPostRequest(restTemplate, role, url, request);

        return Objects.nonNull(response.getBody()) &&
                isAuthorized(response.getStatusCodeValue()) &&
                response.getBody().contains("USER SAVED");
    }

    private static ResponseEntity<String> getStringResponseEntityByPostRequest(TestRestTemplate restTemplate, Role role, String url, HttpEntity request) {
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.CHECKPOINT);
        switch (role) {
            case ADMIN:
                response = postToServerByAdmin(url, request, restTemplate);
                break;
            case USER:
                response = postToServerByUser(url, request, restTemplate);
                break;
        }
        return response;
    }

    public static ResponseEntity<String> postToServerByAdmin(String url, HttpEntity request, TestRestTemplate restTemplate) {
        return restTemplate.withBasicAuth(USERNAME_WRITE, PASSWORD_WRITE).postForEntity(url, request, String.class);
    }

    private static ResponseEntity<String> postToServerByUser(String url, HttpEntity request, TestRestTemplate restTemplate) {
        return restTemplate.withBasicAuth(USERNAME_READ, PASSWORD_READ).postForEntity(url, request, String.class);
    }

    public static boolean createAccount(Account account, TestRestTemplate restTemplate, Role role) {
        String url = "/host/create/account";
        HttpEntity<Account> request = new HttpEntity<>(account);

        ResponseEntity<String> response = getStringResponseEntityByPostRequest(restTemplate, role, url, request);

        return Objects.nonNull(response.getBody()) &&
                isAuthorized(response.getStatusCodeValue()) &&
                response.getBody().contains("ACCOUNT SAVED");
    }

    public static boolean removeUser(User user, TestRestTemplate restTemplate, Role role) {
        String url = "/host/remove/user";
        HttpEntity<User> request = new HttpEntity<>(user);

        ResponseEntity<String> response = getStringResponseEntityByPostRequest(restTemplate, role, url, request);

        return Objects.nonNull(response.getBody()) &&
                isAuthorized(response.getStatusCodeValue()) &&
                response.getBody().contains("removed");
    }

    public static boolean removeAccount(Account account, TestRestTemplate restTemplate, Role role) {
        String url = "/host/remove/account";
        HttpEntity<Account> request = new HttpEntity<>(account);

        ResponseEntity<String> response = getStringResponseEntityByPostRequest(restTemplate, role, url, request);

        return Objects.nonNull(response.getBody()) &&
                isAuthorized(response.getStatusCodeValue()) &&
                response.getBody().contains("removed");
    }
    private static boolean isAuthorized(int responseStatusCode){
        return responseStatusCode == 200;
    }

    public static AccountDTO getAccountDTOFromAccount(Account account){
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCardNumber(account.getCardNumber());
        accountDTO.setPinCode(account.getPinCode());
        return accountDTO;
    }
}
