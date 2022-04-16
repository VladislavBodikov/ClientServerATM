package ru.server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.server.model.entity.Account;
import ru.server.model.entity.User;
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
        boolean isInputDataNotValid = bindingResult.hasErrors();
        if (isInputDataNotValid) {
            return responseIfInputDataNotValid();
        }

        Optional<User> savedUser = userService.save(user);
        boolean isUserSavedSuccess = savedUser.isPresent();
        if (isUserSavedSuccess) {
            String userSavedSuccessful = userSavedSuccessful(savedUser.get());
            log.info(userSavedSuccessful);
            return userSavedSuccessful;
        }

        return userAlreadyExist(user);
    }

    private String responseIfInputDataNotValid() {
        return "Invalid data to save user, check your fields!";
    }

    private String userSavedSuccessful(User savedUser) {
        return "\nUSER SAVED: " + savedUser + "\n";
    }

    private String userAlreadyExist(User inputUser) {
        return "\nuser with same data : " + inputUser + "already exist!" + "\n";
    }

    @PostMapping("/account")
    public String createAccount(@RequestBody Account account) {
        Optional<Account> savedAccount = accountService.save(account);

        boolean isAccountSaved = savedAccount.isPresent();

        if (isAccountSaved) {
            String accountSavedSuccessful = accountSavedSuccess(savedAccount.get());
            log.info(accountSavedSuccessful);
            return accountSavedSuccessful;
        }
        return accountAlreadyExist(account);
    }

    private String accountSavedSuccess(Account account){
        return "\nACCOUNT SAVED: " + account + "\n";
    }

    private String accountAlreadyExist(Account account) {
        return "\nAccount with (card_number || score_number) already exist! : " + account + "\n";
    }
}
