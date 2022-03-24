package ru.client.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import ru.client.dto.AccountDTO;
import ru.client.dto.BalanceDTO;
import ru.client.service.ATMService;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
@Slf4j
public class ATMRestController {

    private final String SERVER_URL = "http://localhost:8082/host/balance";
    @Autowired
    private ATMService atmService;
//    @Autowired
//    private RestTemplate restTemplate;

    @PostMapping(path = "/balance", consumes = "application/json")
    public String balance(@RequestBody AccountDTO accountDTO) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<AccountDTO> request = new HttpEntity<>(accountDTO);
        log.info(request.toString());

        ResponseEntity<BalanceDTO> response = restTemplate.postForEntity(SERVER_URL, request, BalanceDTO.class);
        log.info(response.toString());

        return atmService.showBalance(response.getBody());
    }
}
