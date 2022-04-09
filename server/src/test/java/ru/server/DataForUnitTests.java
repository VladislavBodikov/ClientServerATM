package ru.server;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.server.entity.Account;
import ru.server.entity.Role;
import ru.server.entity.User;

import java.math.BigDecimal;
import java.util.List;

public class DataForUnitTests {

    //Basic auth to WRITE DATA
    public static String USERNAME_WRITE = "admin";
    public static String PASSWORD_WRITE = "admin";
    //Basic auth to READ DATA
    public static String USERNAME_READ = "user";
    public static String PASSWORD_READ = "user";

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

    public static void clearRepos(List<CrudRepository> crudRepositories) {
        crudRepositories.forEach(CrudRepository::deleteAll);
    }

    public static boolean createUser(User user, TestRestTemplate restTemplate, Role role) {
        String url = "/host/create/user";
        HttpEntity<User> request = new HttpEntity<>(user);

        ResponseEntity<String> response = getStringResponseEntityByPostRequest(restTemplate, role, url, request);

        return response.getBody().contains("USER SAVED");
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

        return response.getBody().contains("ACCOUNT SAVED");
    }

    public static boolean removeUser(User user, TestRestTemplate restTemplate, Role role) {
        String url = "/host/remove/user";
        HttpEntity<User> request = new HttpEntity<>(user);

        ResponseEntity<String> response = getStringResponseEntityByPostRequest(restTemplate, role, url, request);

        return response.getBody().contains("removed");
    }

    public static boolean removeAccount(Account account, TestRestTemplate restTemplate, Role role) {
        String url = "/host/remove/account";
        HttpEntity<Account> request = new HttpEntity<>(account);

        ResponseEntity<String> response = getStringResponseEntityByPostRequest(restTemplate, role, url, request);

        return response.getBody().contains("removed");
    }
}
