package ru.server.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.repository.CrudRepository;
import ru.server.entity.Account;
import ru.server.entity.Role;
import ru.server.entity.User;
import ru.server.repository.AccountCrudRepository;
import ru.server.repository.UserCrudRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.server.DataForUnitTests.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RemoveDataControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserCrudRepository userRepository;
    @Autowired
    private AccountCrudRepository accountRepository;

    private List<CrudRepository> repositories;

    private void clearRepositories() {
        if (repositories == null) {
            repositories = new ArrayList() {{
                add(accountRepository);
                add(userRepository);
            }};
        }
        clearRepos(repositories);
    }

    @Test
    @DisplayName("REMOVE USER by Name- success (enough permission)")
    public void removeUserByNameSuccess() {
        User user = getUserWithoutId();

        boolean isSaveUser = createUser(user, restTemplate, Role.ADMIN);
        boolean isRemoveUser = removeUser(user, restTemplate, Role.ADMIN);

        clearRepositories();

        assertAll(
                () -> assertTrue(isRemoveUser),
                () -> assertTrue(isSaveUser)
        );
    }

    @Test
    @DisplayName("REMOVE USER by Name- failure (user not exist)")
    public void removeUserByNameUserNotExistFailure() {
        User user = getUserWithoutId();

        boolean isRemoveUser = removeUser(user, restTemplate, Role.ADMIN);

        clearRepositories();

        assertFalse(isRemoveUser);
    }

    @Test
    @DisplayName("REMOVE USER by Name- failure (don`t have enough permission)")
    public void removeUserByNameFailure() {
        User user = getUserWithoutId();

        boolean isSaveUser = createUser(user, restTemplate, Role.ADMIN);
        boolean isRemoveUser = removeUser(user, restTemplate, Role.USER);

        clearRepositories();

        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertFalse(isRemoveUser)
        );
    }

    @Test
    @DisplayName("REMOVE USER by Id - success (enough permission)")
    public void removeUserByIdSuccess() {
        User user = getUserWithoutId();

        boolean isSaveUser = createUser(user, restTemplate, Role.ADMIN);
        long savedUserId = userRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName()).get().getId();
        boolean isRemoveUser = removeUserByIdBasicAuth(savedUserId, USERNAME_WRITE, PASSWORD_WRITE);

        clearRepositories();

        assertAll(
                () -> assertTrue(isRemoveUser),
                () -> assertTrue(isSaveUser)
        );
    }

    public boolean removeUserByIdBasicAuth(long id, String username, String password) {
        List<User> usersBeforeDelete = new ArrayList<>();
        userRepository.findAll().forEach(usersBeforeDelete::add);

        String url = "/host/remove/user/" + id;

        restTemplate.withBasicAuth(username, password).delete(url);

        // check
        List<User> usersAfterDelete = new ArrayList<>();
        userRepository.findAll().forEach(usersAfterDelete::add);

        return (usersBeforeDelete.size() - usersAfterDelete.size()) == 1;
    }

    @Test
    @DisplayName("REMOVE USER by Id - failure (don`t have enough permission)")
    public void removeUserByIdFailure() {
        User user = getUserWithoutId();

        boolean isSaveUser = createUser(user, restTemplate, Role.ADMIN);
        boolean isRemoveUser = removeUserByIdBasicAuth(1, USERNAME_READ, PASSWORD_READ);

        clearRepositories();

        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertFalse(isRemoveUser)
        );
    }

    @Test
    @DisplayName("REMOVE USER by Id - failure (user don`t exist)")
    public void removeUserByIdFailureUserNotExist() {

        boolean isRemoveUser = removeUserByIdBasicAuth(1, USERNAME_WRITE, PASSWORD_WRITE);

        assertFalse(isRemoveUser);
    }

    @Test
    @DisplayName("REMOVE ACCOUNT by accountNumber- success")
    void removeAccountSuccess() {
        Account account = getAccountWithoutId();

        boolean isSaveUser = createUser(account.getUser(), restTemplate, Role.ADMIN);
        boolean isSaveAccount = createAccount(account, restTemplate, Role.ADMIN);
        boolean isRemoveAccount = removeAccount(account, restTemplate, Role.ADMIN);
        boolean isRemoveUser = removeUser(account.getUser(), restTemplate, Role.ADMIN);

        clearRepositories();

        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertTrue(isSaveAccount),
                () -> assertTrue(isRemoveAccount),
                () -> assertTrue(isRemoveUser)
        );
    }

    @Test
    @DisplayName("REMOVE ACCOUNT by accountNumber- failure (account not exist)")
    void removeAccountDoesNotExist() {
        Account account = getAccountWithoutId();

        boolean isRemoveAccount = removeAccount(account, restTemplate, Role.ADMIN);

        clearRepositories();

        assertFalse(isRemoveAccount);
    }

    @Test
    @DisplayName("REMOVE ACCOUNT by Id - failure (account not found to remove)")
    public void removeAccountByIdAccountNotExistFailure() {
        long randomId = Math.round(Math.random() * 100);

        boolean isRemoveAccount = removeAccountByIdBasicAuth(randomId, USERNAME_WRITE, PASSWORD_WRITE);

        clearRepositories();

        assertFalse(isRemoveAccount);
    }

    @Test
    @DisplayName("REMOVE ACCOUNT by Id - success (enough permission)")
    public void removeAccountByIdSuccess() {
        User user = getUserWithoutId();
        Account account = getAccountWithoutId();

        boolean isSaveUser = createUser(user, restTemplate, Role.ADMIN);
        boolean isSavedAccount = createAccount(account, restTemplate, Role.ADMIN);
        long savedAccountId = accountRepository.findByAccountNumber(account.getAccountNumber()).get().getId();
        boolean isRemoveAccount = removeAccountByIdBasicAuth(savedAccountId, USERNAME_WRITE, PASSWORD_WRITE);

        clearRepositories();

        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertTrue(isSavedAccount),
                () -> assertTrue(isRemoveAccount)
        );
    }

    public boolean removeAccountByIdBasicAuth(long id, String username, String password) {
        List<Account> accountsBeforeDelete = new ArrayList<>();
        accountRepository.findAll().forEach(accountsBeforeDelete::add);

        String url = "/host/remove/account/" + id;

        restTemplate.withBasicAuth(username, password).delete(url);

        // check
        List<Account> accountsAfterDelete = new ArrayList<>();
        accountRepository.findAll().forEach(accountsAfterDelete::add);

        return (accountsBeforeDelete.size() - accountsAfterDelete.size()) == 1;
    }

    @Test
    @DisplayName("REMOVE ACCOUNT by Id - failure (don`t have enough permission)")
    public void removeAccountByIdFailure() {
        User user = getUserWithoutId();
        Account account = getAccountWithoutId();

        boolean isSaveUser = createUser(user, restTemplate, Role.ADMIN);
        boolean isSaveAccount = createAccount(account, restTemplate, Role.ADMIN);
        long savedAccountId = accountRepository.findByAccountNumber(account.getAccountNumber()).get().getId();
        boolean isRemoveAccount = removeAccountByIdBasicAuth(savedAccountId, USERNAME_READ, PASSWORD_READ);

        clearRepositories();

        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertTrue(isSaveAccount),
                () -> assertFalse(isRemoveAccount)
        );
    }
}


