package ru.client.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
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
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @PostMapping(path = "/balance", consumes = "application/json")
    public String balance(@RequestBody AccountDTO accountDTO) {
        setRestTemplateWithBasicAuth(accountDTO);
        //restTemplate = getRestTemplateWithBasicAuth(accountDTO);

        ResponseEntity<BalanceDTO> response = requestBalanceByAccountDTOData(accountDTO);

        return atmService.printBalanceResponse(response);
    }
    //stub
    private RestTemplate getRestTemplateWithBasicAuth(AccountDTO accountDTO){
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();

        String username = accountDTO.getCardNumber();
        String password = accountDTO.getPinCode();
        return restTemplate = restTemplateBuilder
                .basicAuthentication(username, password)
                .build();
    }
    private void setRestTemplateWithBasicAuth(AccountDTO accountDTO){
        String username = accountDTO.getCardNumber();
        String password = accountDTO.getPinCode();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(username,password));
    }

    private ResponseEntity<BalanceDTO> requestBalanceByAccountDTOData(AccountDTO accountDTO){
        HttpEntity<AccountDTO> request = new HttpEntity<>(accountDTO);
        log.debug("REQUEST : " + request);

        ResponseEntity<BalanceDTO> response;
        try {
            response = restTemplate.postForEntity(SERVER_URL, request, BalanceDTO.class);
        } catch (RestClientException exception) {
            log.error(exception.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        log.debug("RESPONSE : " + response);
        return response;
    }

    @GetMapping("/")
    public String getIndex(){
        return "index";
    }

}
