package ru.client.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
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

    private ATMService atmService;
    private RestTemplate restTemplate;

    @PostMapping(path = "/balance", consumes = "application/json")
    public String balance(@RequestBody AccountDTO accountDTO) {
        HttpEntity<AccountDTO> request = new HttpEntity<>(accountDTO);
        log.debug("REQUEST : " + request);

        ResponseEntity<BalanceDTO> response;
        try {
            response = restTemplate.postForEntity(SERVER_URL, request, BalanceDTO.class);
        } catch (RestClientException exception) {
            log.error(exception.getMessage());
            return exception.getMessage();
        }
        log.debug("RESPONSE : " + response);

        return (response.getBody() == null) ? "ERROR : Response.body == null" : atmService.showBalance(response.getBody());
    }

    @GetMapping("/")
    public String getIndex(){
        return "index";
    }

    private HttpHeaders getAuthHeaders(AccountDTO accountDTO){
        HttpHeaders headers = new HttpHeaders();

        String login = accountDTO.getCardNumber();
        String password = accountDTO.getPinCode();

        headers.setBasicAuth(login,password);
        return headers;
    }
}
