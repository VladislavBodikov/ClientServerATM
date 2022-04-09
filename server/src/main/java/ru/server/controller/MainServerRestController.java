package ru.server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.server.dto.AccountDTO;
import ru.server.dto.BalanceDTO;
import ru.server.dto.TransactionDTO;
import ru.server.entity.Account;
import ru.server.exeption.AccountNotFoundException;
import ru.server.exeption.DontHaveEnoughMoneyException;
import ru.server.service.AccountService;
import ru.server.service.UserService;

import javax.validation.Valid;
import java.math.BigDecimal;
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
                prepareBalanceToResponseSuccess(responseBalance, accountFromDb);
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

    private void prepareBalanceToResponseSuccess(BalanceDTO responseBalance, Account accountDB) {
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
            prepareResponseBalanceIfDontHaveEnoughMoneyToTransfer(responseBalance, moneyEx);
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

    private void prepareResponseBalanceIfDontHaveEnoughMoneyToTransfer(BalanceDTO responseBalance, DontHaveEnoughMoneyException e) {
        responseBalance.setStatus(HttpStatus.BAD_GATEWAY);
    }
    private void prepareResponseBalanceIfAccountsForTransferNotFound(BalanceDTO responseBalance, AccountNotFoundException e) {
        responseBalance.setStatus(HttpStatus.EXPECTATION_FAILED);
    }
}
