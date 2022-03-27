package ru.server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.server.dto.AccountDTO;
import ru.server.dto.BalanceDTO;
import ru.server.entity.Account;
import ru.server.entity.User;
import ru.server.service.AccountService;
import ru.server.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/host")
@Slf4j
public class HostRestController {

    private AccountService accountService;

    private UserService userService;

    @PostMapping(value = "/balance",consumes = "application/json")
    public BalanceDTO getBalance(@Valid @RequestBody AccountDTO accountDTO, BindingResult bindingResult){
        BalanceDTO responseBalance = new BalanceDTO();
        // if not valid input data
        if (bindingResult.hasErrors()){
            prepareBalanceToResponseInvalidInputData(responseBalance);
            return responseBalance;
        }

        Optional<Account> accountFromDB = accountService.findByCardNumber(accountDTO.getCardNumber());
        if (accountFromDB.isPresent()){
            Account accountDB = accountFromDB.get();
            if (isCorrectPinCode(accountDTO, accountDB)){
                prepareBalanceToResponsePinCodeIsCorrect(responseBalance, accountDB);
            }
            else {
                prepareBalanceToResponsePinCodeIsNotCorrect(responseBalance);
            }
        }
        return responseBalance;
    }

    private void prepareBalanceToResponseInvalidInputData(BalanceDTO responseBalance) {
        responseBalance.setStatus(HttpStatus.BAD_REQUEST);
    }

    private void prepareBalanceToResponsePinCodeIsNotCorrect(BalanceDTO responseBalance) {
        responseBalance.setStatus(HttpStatus.EXPECTATION_FAILED);
    }

    private void prepareBalanceToResponsePinCodeIsCorrect(BalanceDTO responseBalance, Account accountDB) {
        responseBalance.setCardNumber(accountDB.getCardNumber());
        responseBalance.setAmount(accountDB.getAmount());
        responseBalance.setStatus(HttpStatus.OK);
    }

    private boolean isCorrectPinCode(AccountDTO accountDTO, Account accountFromDB) {
        String inputPinCode = accountDTO.getPinCode();
        String PinCodeFromDB = accountFromDB.getPinCode();
        return inputPinCode.equals(PinCodeFromDB);
    }

    @GetMapping(value = "/balance/error")
    public String sendBalanceError(){
        return "\nAccount data has no valid!\n";
    }

    @GetMapping("/accounts")
    public String getScores() {
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

    @PostMapping("/create/account")
    public String createAccount(@Valid @RequestBody Account account) {
        Optional<User> userFromDB = userService.findByNameIfNotHaveId(account.getUser());
        // if user with ID exist
        if (userFromDB.isPresent()) {
            account.setUser(userFromDB.get());
            Optional<Account> savedAccount = accountService.save(account);
            // if account saved (has not duplicate by card_number or score_number)
            if (savedAccount.isPresent()) {
                log.info("\naccount saved :" + savedAccount.get());
                return "\nACCOUNT SAVED: " + account + "\n";
            }
            return "Account with (card_number | score_number) already exist! : " + account + "\n";
        }
        return "User :" + account.getUser() + " not found!" + "\n";
    }

    @PostMapping("/create/user")
    public String createUser(@Valid @RequestBody User user) {
        Optional<User> savedUser = userService.save(user);
        // if user was saved
        if (savedUser.isPresent()) {
            log.info("\nuser saved : " + savedUser.get());
            return "\nUSER SAVED: " + savedUser.get() + "\n";
        } else {
            return "\nuser with same data already exist!" + "\n";
        }
    }

    @PostMapping("/remove/user")
    public String removeUser(@Valid @RequestBody User user) {
        int changedRows = userService.removeByFirstNameAndLastName(user);

        if (changedRows > 0) {
            log.info("\nuser removed: " + user);
            return "\nuser removed: " + user + "\n";
        }
        return "\nuser not found to remove\n";
    }

    @PostMapping("/remove/account")
    public String removeAccount(@Valid @RequestBody Account account) {
        int changedRows = accountService.removeByScoreNumber(account.getScoreNumber());

        if (changedRows > 0) {
            log.info("\naccount removed: " + account);
            return "\naccount " + " removed" + account + "\n";
        }
        return "\naccount not found to remove\n";
    }
}
