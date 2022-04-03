package ru.server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.server.dto.AccountDTO;
import ru.server.dto.BalanceDTO;
import ru.server.entity.Account;
import ru.server.service.AccountService;
import ru.server.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/host")
@Slf4j
public class MainServerRestController {

    private AccountService accountService;

    private UserService userService;

    @PostMapping(value = "/balance", consumes = "application/json")
    public BalanceDTO getBalance(@Valid @RequestBody AccountDTO accountDTO, BindingResult bindingResult) {
        BalanceDTO responseBalance = new BalanceDTO();

        boolean isInputDataNotValid = bindingResult.hasErrors();
        if (isInputDataNotValid) {
            prepareBalanceToResponseInvalidInputData(responseBalance);
            return responseBalance;
        }

        Optional<Account> accountFromDB = accountService.findByCardNumber(accountDTO.getCardNumber());
        boolean isAccountFound = accountFromDB.isPresent();
        if (isAccountFound) {
            Account accountFromDb = accountFromDB.get();
            String incomingPin = accountDTO.getPinCode();
            String databasePin = accountFromDb.getPinCode();
            
            if (isPinCorrect(incomingPin, databasePin)) {
                prepareBalanceToResponsePinCorrect(responseBalance, accountFromDb);
            } else {
                prepareBalanceToResponsePinNotCorrect(responseBalance);
            }
        }
        return responseBalance;
    }

    private void prepareBalanceToResponseInvalidInputData(BalanceDTO responseBalance) {
        responseBalance.setStatus(HttpStatus.BAD_REQUEST);
    }

    private boolean isPinCorrect(String inputPin, String pinFromDatabase) {
        return inputPin.equals(pinFromDatabase);
    }

    private void prepareBalanceToResponsePinCorrect(BalanceDTO responseBalance, Account accountDB) {
        responseBalance.setCardNumber(accountDB.getCardNumber());
        responseBalance.setAmount(accountDB.getAmount());
        responseBalance.setStatus(HttpStatus.OK);
    }

    private void prepareBalanceToResponsePinNotCorrect(BalanceDTO responseBalance) {
        responseBalance.setStatus(HttpStatus.EXPECTATION_FAILED);
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
}
