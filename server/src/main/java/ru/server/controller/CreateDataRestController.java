package ru.server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.server.entity.Account;
import ru.server.entity.User;
import ru.server.service.AccountService;
import ru.server.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/host/create")
@AllArgsConstructor
@Slf4j
public class CreateDataRestController {

    private AccountService accountService;

    private UserService userService;

    @PostMapping("/user")
    public String createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        // if not valid input data
        if (bindingResult.hasErrors()){
            return responseIfInputDataNotValid();
        }

        Optional<User> savedUser = userService.save(user);
        if (savedUser.isPresent()) {
            log.info("\nuser saved : " + savedUser.get());
            return "\nUSER SAVED: " + savedUser.get() + "\n";
        } else {
            return "\nuser with same data already exist!" + "\n";
        }
    }

    @PostMapping("/account")
    public String createAccount(@Valid @RequestBody Account account,BindingResult bindingResult) {
        // if not valid input data
        if (bindingResult.hasErrors()){
            return responseIfInputDataNotValid();
        }

        Optional<User> userFromDB = userService.findByNameIfNotHaveId(account.getUser());
        // if user with account.user.id exist
        if (userFromDB.isPresent()) {
            account.setUser(userFromDB.get());
            Optional<Account> savedAccount = accountService.save(account);
            // if account saved (has not duplicate by card_number or score_number)
            if (savedAccount.isPresent()) {
                log.info("\naccount saved :" + savedAccount.get());
                return "\nACCOUNT SAVED: " + account + "\n";
            }
            return "\nAccount with (card_number or score_number) already exist! : " + account + "\n";
        }
        return "\nUser :" + account.getUser() + " not found!" + "\n";
    }

    private String responseIfInputDataNotValid() {
        return "Invalid data to save user, check your fields!";
    }
}
