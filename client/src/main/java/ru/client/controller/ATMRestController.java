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
    public String balance(@RequestBody AccountDTO incomeAuthData) {
        setRestTemplateWithBasicAuth(incomeAuthData);

        ResponseEntity<BalanceDTO> response = requestBalance(incomeAuthData);

        return atmService.printBalanceResponse(response);
    }

    private void setRestTemplateWithBasicAuth(AccountDTO authenticationData){
        restTemplate.getInterceptors().clear();

        String username = authenticationData.getCardNumber();
        String password = authenticationData.getPinCode();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(username,password));
    }

    private ResponseEntity<BalanceDTO> requestBalance(AccountDTO requestAuthData){
        HttpEntity<AccountDTO> request = new HttpEntity<>(requestAuthData);
        log.info("REQUEST : " + request);

        ResponseEntity<BalanceDTO> response;
        try {
            response = restTemplate.postForEntity(SERVER_URL + BALANCE_URL, request, BalanceDTO.class);
        } catch (RestClientException exception) {
            log.error("RESPONSE : " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        log.info("RESPONSE : " + response);
        return response;
    }

    @PostMapping(path = "/money/transfer", consumes = "application/json")
    public String sendMoney(@RequestBody TransactionDTO transactionDTO){
        if (isAttemptToSendMoneyToSameCard(transactionDTO.getAccountFrom().getCardNumber(),transactionDTO.getCardNumberTo())){
            return "\nTried to send money to the same card!\n";
        }

        AccountDTO authData = transactionDTO.getAccountFrom();
        setRestTemplateWithBasicAuth(authData);

        ResponseEntity<BalanceDTO> balanceAfterTransfer = transfer(transactionDTO);
        return atmService.printResultOfTransaction(balanceAfterTransfer);
    }

    private boolean isAttemptToSendMoneyToSameCard(String cardFrom, String cardTo){
        return cardFrom.equals(cardTo);
    }

    private ResponseEntity<BalanceDTO> transfer(TransactionDTO transactionDTO){
        HttpEntity<TransactionDTO> request = new HttpEntity<>(transactionDTO);
        log.debug("REQUEST : " + request);

        ResponseEntity<BalanceDTO> response;
        try {
            response = restTemplate.postForEntity(SERVER_URL + MONEY_TRANSFER_URL, request, BalanceDTO.class);
        } catch (RestClientException exception) {
            log.error(exception.getMessage());
            BalanceDTO balanceResponse = new BalanceDTO();
            balanceResponse.setMessage("WRONG PIN-CODE!");
            return new ResponseEntity<>(balanceResponse,HttpStatus.CONFLICT);
        }
        log.debug("RESPONSE : " + response);
        return response;
    }

}
