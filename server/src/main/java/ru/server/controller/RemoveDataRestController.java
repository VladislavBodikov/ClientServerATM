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

        boolean isSomeRowsWasChangedInDatabase = changedRows > 0;
        if (isSomeRowsWasChangedInDatabase) {
            String userRemovedSuccess = userRemovedSuccessful(user);
            log.info(userRemovedSuccess);
            return userRemovedSuccess;
        }
        return userNotFoundToRemove(user);
    }

    private String userRemovedSuccessful(User user) {
        return "\nuser removed: " + user + "\n";
    }

    private String userNotFoundToRemove(User user) {
        return "\nuser not found to remove" + user + "\n";
    }

    @DeleteMapping("/user/{id}")
    public String removeUserById(@PathVariable Long id) {
        int changedRows = userService.removeById(id);

        boolean isSomeRowsWasChangedInDatabase = changedRows > 0;
        if (isSomeRowsWasChangedInDatabase) {
            String userRemovedSuccess = userRemovedByIdSuccess(id);
            log.info(userRemovedSuccess);
            return userRemovedSuccess;
        }
        return userNotFoundByIdToRemove(id);
    }

    private String userRemovedByIdSuccess(Long id) {
        return "\nuser with ID: " + id + " removed";
    }

    private String userNotFoundByIdToRemove(Long id) {
        return "\nuser with ID: " + id + " not found to remove\n";
    }

    @PostMapping("/account")
    public String removeAccount(@Valid @RequestBody Account account) {
        int changedRows = accountService.removeByAccountNumber(account.getAccountNumber());

        boolean isSomeRowsWasChangedInDatabase = changedRows > 0;
        if (isSomeRowsWasChangedInDatabase) {
            String accountRemovedSuccessful = accountRemovedSuccessful(account);
            log.info(accountRemovedSuccessful);
            return accountRemovedSuccessful;
        }
        return accountNotFoundToRemove(account);
    }

    private String accountRemovedSuccessful(Account account) {
        return "\naccount removed: " + account + "\n";
    }

    private String accountNotFoundToRemove(Account account) {
        return "\naccount not found to remove" + account + "\n";
    }

    @DeleteMapping("/account/{id}")
    public String removeAccountById(@PathVariable Long id) {
        int changedRows = accountService.removeById(id);

        boolean isSomeRowsWasChangedInDatabase = changedRows > 0;
        if (isSomeRowsWasChangedInDatabase) {
            String accountRemovedSuccess = accountRemovedByIdSuccess(id);
            log.info(accountRemovedSuccess);
            return accountRemovedSuccess;
        }
        return accountNotFoundToRemoveById(id);
    }

    private String accountRemovedByIdSuccess(Long id) {
        return "\naccount with ID: " + id + " removed\n";
    }

    private String accountNotFoundToRemoveById(Long id) {
        return "\naccount with ID: " + id + "not found to remove\n";
    }
}
