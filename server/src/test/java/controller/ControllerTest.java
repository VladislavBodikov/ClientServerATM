package controller;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import ru.server.Application;
import ru.server.controller.HostRestController;
import ru.server.dto.AccountDTO;
import ru.server.dto.BalanceDTO;
import ru.server.entity.Account;
import ru.server.entity.User;
import ru.server.repository.UserCrudRepository;
import ru.server.service.UserService;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@SpringBootTest(classes = {Application.class})
//@AutoConfigureMockMvc

//@WebMvcTest(HostRestController.class)
//@ContextConfiguration(classes = {Application.class,HostRestController.class})

//@SpringBootTest(classes = {Application.class})
public class ControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
    //@Autowired
    private RestTemplate restTemplate;



    @Test
    @DisplayName("Создание пользователя в базе")
    public void createUser() throws Exception {
        final String serverURL = "http://localhost:8082/host/create/user";

        User user = new User();
        user.setFirstName("Vlad");
        user.setLastName("Bodik");
        restTemplate = new RestTemplate();
        HttpEntity<User> request = new HttpEntity<>(user);

        ResponseEntity<String> response = restTemplate.exchange(serverURL, HttpMethod.POST, request, String.class);

        String responseBody = response.getBody();
        assertAll(
                ()->assertTrue(responseBody.contains("USER SAVED:")),
                ()->assertTrue(responseBody.contains("Vlad")),
                ()->assertFalse(responseBody.contains("id=null")),
                ()->assertTrue(responseBody.contains("Bodik"))
        );
    }
    @Test
    @DisplayName("Сохранение счета в базе - УСПЕХ")
    void createScoreSuccess(){
        final String serverURL = "http://localhost:8082/host/create/score";

        Account account = new Account();
        account.setUser(new User());
        account.getUser().setId(1);
        account.setAmount(new BigDecimal("9999.0111"));
        account.setCardNumber("1234");
        account.setScoreNumber("4321");
        account.setPinCode("1111");

        restTemplate = new RestTemplate();
        HttpEntity<Account> request = new HttpEntity<>(account);
        ResponseEntity<String> response = restTemplate.exchange(serverURL, HttpMethod.POST, request, String.class);

        String responseBody = response.getBody();
        assertAll(
                ()->assertTrue(responseBody.contains("SCORE SAVED:")),
                ()->assertTrue(responseBody.contains("scoreNumber=4321")),
                ()->assertFalse(responseBody.contains("id=null")),
                ()->assertTrue(responseBody.contains("pinCode=1111"))
        );
    }
    @Test
    @DisplayName("Сохранение счета в базе - ПРОВАЛ (юзер не найден)")
    void createScoreFailure(){
        final String serverURL = "http://localhost:8082/host/create/score";
        final long notExistUserId = Long.MAX_VALUE;

        Account account = new Account();
        account.setUser(new User());
        account.getUser().setId(notExistUserId);
        account.setAmount(new BigDecimal("9999.0111"));
        account.setCardNumber("1234");
        account.setScoreNumber("4321");
        account.setPinCode("1111");

        restTemplate = new RestTemplate();
        HttpEntity<Account> request = new HttpEntity<>(account);
        ResponseEntity<String> response = restTemplate.exchange(serverURL, HttpMethod.POST, request, String.class);

        String responseBody = response.getBody();
        assertAll(
                ()->assertTrue(responseBody.contains("User with ID: " + notExistUserId)),
                ()->assertTrue(responseBody.contains("not found!"))
        );
    }
}
