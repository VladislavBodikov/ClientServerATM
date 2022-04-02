package ru.server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.server.entity.Account;
import ru.server.entity.User;
import ru.server.service.AccountService;
import ru.server.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/host/remove")
@AllArgsConstructor
@Slf4j
public class RemoveDataRestController {

    private AccountService accountService;

    private UserService userService;

    @PostMapping("/user")
    public String removeUser(@Valid @RequestBody User user) {
        int changedRows = userService.removeByFirstNameAndLastName(user);

        if (changedRows > 0) {
            log.info("\nuser removed: " + user);
            return "\nuser removed: " + user + "\n";
        }
        return "\nuser not found to remove\n";
    }

    @PostMapping("/account")
    public String removeAccount(@Valid @RequestBody Account account) {
        int changedRows = accountService.removeByAccountNumber(account.getAccountNumber());

        if (changedRows > 0) {
            log.info("\naccount removed: " + account);
            return "\naccount " + " removed" + account + "\n";
        }
        return "\naccount not found to remove\n";
    }

    @DeleteMapping("/account/{id}")
    public String removeAccountById(@PathVariable Long id){
        int changedRows = accountService.removeById(id);

        if (changedRows > 0) {
            log.info("\naccount with ID: "+ id + " removed");
            return "\naccount with ID: "+ id + " removed\n";
        }
        return "\naccount not found to remove\n";
    }
    @DeleteMapping("/user/{id}")
    public String removeUserById(@PathVariable Long id){
        int changedRows = userService.removeById(id);

        if (changedRows > 0) {
            log.info("\nuser with ID: "+ id + " removed");
            return "\nuser with ID: " + id + " removed\n";
        }
        return "\nuser not found to remove\n";
    }
}
