package ru.client.atmApplication.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.client.atmApplication.service.ATMService;
import ru.client.dto.AccountDTO;
import ru.client.dto.BalanceDTO;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
@Slf4j
public class ATMRestController {

    private ATMService atmService;
    final String serverURL = "http://localhost:8082/host/balance";

    @PostMapping(path = "/balance", consumes = "application/json")
    public String balance(@RequestBody AccountDTO accountDTO) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<AccountDTO> request = new HttpEntity<>(accountDTO);
        log.info(request.toString());

        ResponseEntity<BalanceDTO> response = restTemplate.exchange(serverURL, HttpMethod.POST, request, BalanceDTO.class);
        log.info(response.toString());

        return atmService.showBalance(response.getBody());
    }
}
