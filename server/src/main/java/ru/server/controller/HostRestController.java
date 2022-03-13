package ru.server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.server.entity.Score;
import ru.server.entity.User;
import ru.server.exeptions.ScoreNotFoundException;
import ru.server.service.ScoreService;
import ru.server.service.UserService;

import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping("/host")
@Slf4j
public class HostRestController {

    private ScoreService scoreService;
    private UserService userService;

    @PostMapping("/balance")
    public Score getScore(@RequestBody Score score) {
        Score scoreFromDB = scoreService.findByCardNumber(score.getCardNumber()).orElseThrow(()->new ScoreNotFoundException(""));
        if (score.getPinCode().equals(scoreFromDB.getPinCode())){
            score.setAmount(scoreFromDB.getAmount());
        }
        return score;
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
    public String createScore(@RequestBody Score score) {
        long userId = score.getUser().getId();
        if (userService.isUserExist(userId)){
            Score savedScore = scoreService.save(score);
            log.info("\nscore saved - id : " + savedScore.getId() + " score number : " + savedScore.getScoreNumber());
            return "\nSCORE WAS SAVED: " + score + "\n";
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
