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
    @GetMapping("/test")
    public String showTestCode(){
        User user = userService.findById(2L).get();
        List<Account> account = accountService.getAllScores();
        return "";
    }

    @PostMapping("/create/score")
    public String createScore(@RequestBody Account account) {
        long userId = account.getUser().getId();
        // if user with ID exist
        if (userService.isUserExistById(userId)) {
            User userFromDB = userService.findById(userId).get();
            account.setUser(userFromDB);
            Optional<Account> savedAccount = accountService.save(account);
            // if account saved (has not duplicate by card number)
            if (savedAccount.isPresent()) {
                log.info("\naccount saved - id : " + savedAccount.get().getId() + " score number : " + savedAccount.get().getScoreNumber());
                return "\nACCOUNT SAVED: " + account + "\n";
            }
            return "Account with card_number : " + account.getCardNumber() + "\t already exist!";
        }
        return "User with ID: " + userId + " not found!";
    }

    @PostMapping("/create/user")
    public String createUser(@RequestBody User user) {
        User savedUser = userService.save(user);
        log.info("\nuser saved -  id : " + savedUser.getId() + " name : " + savedUser.getFirstName());
        return "\nUSER SAVED: " + user + "\n";
    }

    @GetMapping("/accounts/{userId}")
    public String getAccounts(@PathVariable long userId){
        Optional<User> userFromBD = userService.findById(userId);
        if (userFromBD.isPresent()){
            User user  = userFromBD.get();
            Set<Account> accounts = user.getAccounts();

            StringBuilder sb = new StringBuilder();
            sb      .append(user)
                    .append(System.lineSeparator())
                    .append("ACCOUNTS: ")
                    .append(System.lineSeparator());
            accounts.forEach((x)->sb.append(x).append(System.lineSeparator()));
            return sb.toString();
        }
        return "user not found";
    }
}
