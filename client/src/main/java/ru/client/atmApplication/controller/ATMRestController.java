package ru.client.atmApplication.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.client.atmApplication.entity.Account;
import ru.client.atmApplication.service.ATMService;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
@Slf4j
public class ATMRestController {

    private ATMService atmService;
    final String hostUrl = "http://localhost:8082/host/balance";

    @PostMapping(path = "/balance", consumes = "application/json")
    public String balance(@RequestBody Account account) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Account> request = new HttpEntity<>(account);
        log.info(request.toString());
        ResponseEntity<Account> response = restTemplate.exchange(hostUrl, HttpMethod.POST, request, Account.class);
        log.info(response.toString());

        return atmService.showBalance(response.getBody());
    }
}
