package ru.server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.server.dto.AccountDTO;
import ru.server.dto.BalanceDTO;
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
    public BalanceDTO getScore(@RequestBody AccountDTO accountDTO) {
        Account accountFromDB = scoreService.findByCardNumber(accountDTO.getCardNumber()).orElseThrow(() -> new ScoreNotFoundException(""));
        BalanceDTO outputBalance = new BalanceDTO();

        String inputPinCode = accountDTO.getPinCode();
        String PinCodeFromDB = accountFromDB.getPinCode();
        // if pin_code is correct
        if (inputPinCode.equals(PinCodeFromDB)) {
            outputBalance.setCardNumber(accountDTO.getCardNumber());
            outputBalance.setAmount(accountFromDB.getAmount());
        }
        return outputBalance;
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
        if (userService.isUserExistById(userId)) {
            Account savedAccount = scoreService.save(account);
            log.info("\nscore saved - id : " + savedAccount.getId() + " score number : " + savedAccount.getScoreNumber());
            return "\nSCORE WAS SAVED: " + account + "\n";
        } else {
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
