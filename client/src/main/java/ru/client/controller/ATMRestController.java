package ru.client.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.client.service.ATMService;
import ru.client.dto.AccountDTO;
import ru.client.dto.BalanceDTO;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
@Slf4j
public class ATMRestController {

    private final String SERVER_URL = "http://localhost:8082/host/balance";

//    @Autowired
    private ATMService atmService;
//    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(path = "/balance", consumes = "application/json")
    public String balance(@RequestBody AccountDTO accountDTO) {
        final String RESPONSE_HAS_NULL_BODY = "ERROR : Response.body == null";

        HttpEntity<AccountDTO> request = new HttpEntity<>(accountDTO);
        log.info("REQUEST : " + request);

        ResponseEntity<BalanceDTO> response;
        try {
            response = restTemplate.postForEntity(SERVER_URL, request, BalanceDTO.class);
        } catch (RestClientException exception) {
            log.error(exception.getMessage());
            return exception.getMessage();
        }
        log.info("RESPONSE : " + response);

        return (response.getBody() == null) ?
                RESPONSE_HAS_NULL_BODY :
                atmService.showBalance(response.getBody());
    }

    @GetMapping("/")
    public String getIndex(){
        return "index";
    }
}
