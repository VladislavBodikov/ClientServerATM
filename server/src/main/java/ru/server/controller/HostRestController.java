package ru.server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.server.entity.Account;
import ru.server.entity.User;
import ru.server.exeptions.ScoreNotFoundException;
import ru.server.service.ScoreService;
import ru.server.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/host")
@Slf4j
public class HostRestController {

    private ScoreService scoreService;
    private UserService userService;

    @PostMapping("/balance")
    public Account getScore(@RequestBody Account account) {
        Account accountFromDB = scoreService.findByCardNumber(account.getCardNumber()).orElseThrow(()->new ScoreNotFoundException(""));
        if (account.getPinCode().equals(accountFromDB.getPinCode())){
            account.setAmount(accountFromDB.getAmount());
        }
        return account;
    }

    @GetMapping("/scores")
    public String getScores() {
        StringBuilder sb = new StringBuilder();
        scoreService.getAllScores().forEach((x) -> sb.append(x.toString()).append(System.lineSeparator()));
        return sb.toString();
    }
    @GetMapping("/users")
    public String getUsers() {
        StringBuilder sb = new StringBuilder();
        userService.getAllUsers().forEach((x) -> sb.append(x.toString()).append(System.lineSeparator()));
        return sb.toString();
    }

    @PostMapping("/create/score")
    public String createScore(@RequestBody Account account) {
        long userId = account.getUser().getId();
        if (userService.isUserExist(userId)){
            Account savedAccount = scoreService.save(account);
            log.info("\nscore saved - id : " + savedAccount.getId() + " score number : " + savedAccount.getScoreNumber());
            return "\nSCORE WAS SAVED: " + account + "\n";
        }
        else {
            return "User with ID: " + userId + " not found!";
        }

    }

    @PostMapping("/create/user")
    public String createUser(@RequestBody User user) {
        User savedUser = userService.save(user);
        log.info("\nuser saved -  id : " + savedUser.getId() + " name : " + savedUser.getFirstName());
        return "\nUSER WAS SAVED: " + user + "\n";
    }
}
