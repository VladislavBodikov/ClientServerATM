package ru.server.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.server.dto.AccountDTO;
import ru.server.dto.BalanceDTO;
import ru.server.entity.Account;
import ru.server.entity.User;
import ru.server.service.AccountService;
import ru.server.service.UserService;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MockTest {
    @MockBean
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
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

    private User getUser() {
        User user = new User();
        user.setId(1);
        user.setFirstName("Vlad");
        user.setLastName("Bodik");
        return user;
    }
    private BalanceDTO getBalance(AccountDTO accountDTO){
        final String getBalanceURL = "http://localhost:8082/host/balance";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<AccountDTO> requestBalance = new HttpEntity<>(accountDTO);
        ResponseEntity<BalanceDTO> responseBalance = restTemplate.postForEntity(getBalanceURL, requestBalance, BalanceDTO.class);
        return responseBalance.getBody();
    }
    private boolean createUser(User user){
//        final String createUserURL = "http://localhost:8082/host/create/user";
//        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<String> response = restTemplate.postForEntity("/host/create/user",request,String.class);

        return response.getBody().contains("USER SAVED");
    }
    private boolean createAccount(Account account){
        final String createAccountURL = "http://localhost:8082/host/create/account";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Account> request = new HttpEntity<>(account);
        ResponseEntity<String> response = restTemplate.postForEntity(createAccountURL,request,String.class);

        return response.getBody().contains("ACCOUNT SAVED");
    }
    private boolean removeUser(User user){
//        final String removeUserURL = "http://localhost:8082/host/remove/user";
//        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<String> response = restTemplate.postForEntity("/host/remove/user",request,String.class);

        return response.getBody().contains("removed");
    }
    private boolean removeAccount(Account account){
        final String removeAccURL = "http://localhost:8082/host/remove/account";

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Account> request = new HttpEntity<>(account);
        ResponseEntity<String> response = restTemplate.postForEntity(removeAccURL,request,String.class);

        return response.getBody().contains("removed");
    }
}
