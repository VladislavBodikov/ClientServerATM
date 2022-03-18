package ru.server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.server.dto.AccountDTO;
import ru.server.dto.BalanceDTO;
import ru.server.entity.Account;
import ru.server.entity.User;
import ru.server.exeptions.ScoreNotFoundException;
import ru.server.service.AccountService;
import ru.server.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/host")
@Slf4j
public class HostRestController {

    private AccountService accountService;
    private UserService userService;

    @PostMapping("/balance")
    public BalanceDTO getBalance(@RequestBody AccountDTO accountDTO) {
        Account accountFromDB = accountService.findByCardNumber(accountDTO.getCardNumber()).orElseThrow(() -> new ScoreNotFoundException(""));
        BalanceDTO outputBalance = new BalanceDTO();

        // if pin_code is correct
        if (isCorrectPinCode(accountDTO, accountFromDB)) {
            outputBalance.setCardNumber(accountDTO.getCardNumber());
            outputBalance.setAmount(accountFromDB.getAmount());
        }
        return outputBalance;
    }

    private boolean isCorrectPinCode(AccountDTO accountDTO, Account accountFromDB) {
        String inputPinCode = accountDTO.getPinCode();
        String PinCodeFromDB = accountFromDB.getPinCode();
        return inputPinCode.equals(PinCodeFromDB);
    }

    @GetMapping("/accounts")
    public String getScores() {
        StringBuilder sb = new StringBuilder();
        accountService.getAllScores().forEach((x) -> sb.append(x.toString()).append(System.lineSeparator()));
        return sb.toString();
    }

    @GetMapping("/users")
    public String getUsers() {
        StringBuilder sb = new StringBuilder();
        userService.getAllUsers().forEach((x) -> sb.append(x.toString()).append(System.lineSeparator()));
        return sb.toString();
    }

    @PostMapping("/create/account")
    public String createAccount(@RequestBody Account account) {
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
            return "Account with (card_number | score_number) already exist! : " + account;
        }
        return "User :" + account.getUser() + " not found!";
    }

    @PostMapping("/create/user")
    public String createUser(@RequestBody User user) {
        Optional<User> savedUser = userService.save(user);
        // if user was saved
        if (savedUser.isPresent()){
            log.info("\nuser saved : " + savedUser);
            return "\nUSER SAVED: " + savedUser + "\n";
        }
        else {
            return "\nuser with same data already exist!";
        }
    }

    @PostMapping("/remove/user")
    public String removeUser(@RequestBody User user){
        int changedRows = userService.removeByFirstNameAndLastName(user);

        if (changedRows > 0){
            log.info("user removed: " + user);
            return "\nuser removed: " + user;
        }
        return "user not found to remove";
    }
    @PostMapping("/remove/account")
    public String removeAccount(@RequestBody Account account){
        int changedRows = accountService.removeByScoreNumber(account.getScoreNumber());

        if (changedRows > 0){
            log.info("account removed: " + account);
            return "\naccount " + " removed" + account;
        }
        return "account not found to remove";
    }
}
