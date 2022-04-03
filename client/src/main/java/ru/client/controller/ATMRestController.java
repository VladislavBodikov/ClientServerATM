package ru.client.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.client.dto.AccountDTO;
import ru.client.dto.BalanceDTO;
import ru.client.dto.TransactionDTO;
import ru.client.service.ATMService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
@Slf4j
public class ATMRestController {

    private final String SERVER_URL = "http://localhost:8082/host";
    private final String BALANCE_URL = "/balance";
    private final String MONEY_TRANSFER_URL = "/money/transfer";

    private ATMService atmService;
    private RestTemplate restTemplate;

    @PostMapping(path = "/balance", consumes = "application/json")
    public String balance(@RequestBody AccountDTO accountDTO) {
        setRestTemplateWithBasicAuth(accountDTO);

        ResponseEntity<BalanceDTO> response = requestBalance(accountDTO);

        return atmService.printBalanceResponse(response);
    }

    private void setRestTemplateWithBasicAuth(AccountDTO accountDTO){
        String username = accountDTO.getCardNumber();
        String password = accountDTO.getPinCode();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(username,password));
    }

    private ResponseEntity<BalanceDTO> requestBalance(AccountDTO accountDTO){
        HttpEntity<AccountDTO> request = new HttpEntity<>(accountDTO);
        log.debug("REQUEST : " + request);

        ResponseEntity<BalanceDTO> response;
        try {
            response = restTemplate.postForEntity(SERVER_URL + BALANCE_URL, request, BalanceDTO.class);
        } catch (RestClientException exception) {
            log.error(exception.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        log.debug("RESPONSE : " + response);
        return response;
    }

    @PostMapping(path = "/sendMoney", consumes = "application/json")
    public String sendMoney(@RequestBody TransactionDTO transactionDTO){
        setRestTemplateWithBasicAuth(transactionDTO.getAccountFrom());

        ResponseEntity<BalanceDTO> balanceBeforeTransfer = requestBalance(transactionDTO.getAccountFrom());
        ResponseEntity<BalanceDTO> balanceAfterTransfer;

        if (balanceBeforeTransfer.getBody().getAmount().subtract(transactionDTO.getAmountToTransfer()).compareTo(new BigDecimal(0)) >= 0){
            balanceAfterTransfer = transfer(transactionDTO);
            return atmService.printResultOfTransaction(balanceBeforeTransfer,balanceAfterTransfer,transactionDTO.getAmountToTransfer());
        }
        return "\ndont have enough money to transfer\n";
    }

    private ResponseEntity<BalanceDTO> transfer(TransactionDTO transactionDTO){
        HttpEntity<TransactionDTO> request = new HttpEntity<>(transactionDTO);
        log.debug("REQUEST : " + request);

        ResponseEntity<BalanceDTO> response;
        try {
            response = restTemplate.postForEntity(SERVER_URL + MONEY_TRANSFER_URL, request, BalanceDTO.class);
        } catch (RestClientException exception) {
            log.error(exception.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        log.debug("RESPONSE : " + response);
        return response;
    }
}
