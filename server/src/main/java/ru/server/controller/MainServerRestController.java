package ru.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.server.model.dto.AccountDTO;
import ru.server.model.dto.BalanceDTO;
import ru.server.model.dto.TransactionDTO;
import ru.server.model.entity.Account;
import ru.server.exceptions.AccountNotFoundException;
import ru.server.exceptions.DontHaveEnoughMoneyException;
import ru.server.service.AccountService;
import ru.server.service.UserService;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/host")
@Slf4j
public class MainServerRestController {

    private AccountService accountService;

    private UserService userService;

    private KafkaTemplate<String,String> kafkaTemplate;

    private ObjectMapper objectMapper;

    @KafkaListener(topics = "balance-request-topic",groupId = "balance")
    private void listenRequestBalance(String inputAccountData) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        AccountDTO accountDTO = objectMapper.readValue(inputAccountData,AccountDTO.class);
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(accountDTO.getCardNumber(),accountDTO.getPinCode()));
        ResponseEntity<BalanceDTO> response = restTemplate.postForEntity("/host/balance", new HttpEntity<>(accountDTO), BalanceDTO.class);
        kafkaTemplate.send("balance-response-topic","balance","");
    }

    @PostMapping(value = "/balance", consumes = "application/json")
    public BalanceDTO getBalance(@RequestBody AccountDTO accountDTO) {
        BalanceDTO responseBalance = new BalanceDTO();

        Optional<Account> accountFromDB = accountService.findByCardNumber(accountDTO.getCardNumber());
        prepareBalanceToResponseSuccess(responseBalance,accountFromDB.get());

        return responseBalance;
    }

    private void prepareBalanceToResponseSuccess(BalanceDTO responseBalance, Account accountDB) {
        responseBalance.setCardNumber(accountDB.getCardNumber());
        responseBalance.setAmount(accountDB.getAmount());
        responseBalance.setStatus(HttpStatus.OK);
    }

    @GetMapping("/accounts")
    public String getAccounts() {
        StringBuilder sb = new StringBuilder();
        accountService.getAllAccounts().forEach((x) -> sb.append(x.toString()).append(System.lineSeparator()));
        return sb.toString();
    }

    @GetMapping("/users")
    public String getUsers() {
        StringBuilder sb = new StringBuilder();
        userService.getAllUsers().forEach((x) -> sb.append(x.toString()).append(System.lineSeparator()));
        return sb.toString();
    }

    @PostMapping("/money/transfer")
    public BalanceDTO returnBalanceAfterTransaction(@RequestBody TransactionDTO transactionDTO){
        BalanceDTO responseBalance = new BalanceDTO();

        String cardNumberFrom = transactionDTO.getAccountFrom().getCardNumber();
        String cardNumberTo   = transactionDTO.getCardNumberTo();
        BigDecimal amountToTransfer = transactionDTO.getAmountToTransfer();

        Account accountAfterTransfer;
        try{
            accountAfterTransfer = accountService.transactionCardToCard(cardNumberFrom,amountToTransfer,cardNumberTo);
            prepareBalanceToResponseSuccess(responseBalance, accountAfterTransfer);
        }
        catch (DontHaveEnoughMoneyException moneyEx){
            log.info(moneyEx.getMessage());
            prepareResponseBalanceIfDontHaveEnoughMoneyToTransfer(responseBalance);
            return responseBalance;
        }
        catch (AccountNotFoundException accEx){
            log.info(accEx.getMessage());
            prepareResponseBalanceIfAccountsForTransferNotFound(responseBalance,accEx);
            return responseBalance;
        }
        log.info("\nSUCCESS TRANSACTION \nFrom card: " + cardNumberFrom + " to card: " + cardNumberTo + " \nVALUE : " + amountToTransfer + "\n");
        return responseBalance;
    }

    private void prepareResponseBalanceIfDontHaveEnoughMoneyToTransfer(BalanceDTO responseBalance) {
        responseBalance.setStatus(HttpStatus.BAD_GATEWAY);
        responseBalance.setMessage("Don`t have enough amount to transfer!");
    }
    private void prepareResponseBalanceIfAccountsForTransferNotFound(BalanceDTO responseBalance, AccountNotFoundException e) {
        responseBalance.setStatus(HttpStatus.EXPECTATION_FAILED);
        responseBalance.setMessage("Account with card_number to transfer not found!");
    }
}
