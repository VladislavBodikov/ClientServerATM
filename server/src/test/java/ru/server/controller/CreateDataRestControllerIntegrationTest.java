package ru.server.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.server.model.entity.Account;
import ru.server.model.Role;
import ru.server.model.entity.User;
import ru.server.repository.AccountCrudRepository;
import ru.server.repository.UserCrudRepository;

import static org.junit.jupiter.api.Assertions.*;
import static ru.server.DataForUnitTests.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateDataRestControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserCrudRepository userRepository;
    @Autowired
    private AccountCrudRepository accountRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void clearWasteDataAfterTests(){
        clearRepos(userRepository,accountRepository);
    }

    @Test
    @DisplayName("Создание пользователя в базе - УСПЕХ (У авторизованного юзера достаточно прав)")
    public void createUserDBSuccess() {
        User user = getUserWithoutId();

        boolean isSaveUser = createUser(user, restTemplate, Role.ADMIN);

        assertTrue(isSaveUser);
    }

    @Test
    @DisplayName("Создание пользователя в базе - ПРОВАЛ (У авторизованного юзера недостаточно прав)")
    public void createUserDBFailure() {
        User user = getUserWithoutId();

        boolean isSaveUser = createUser(user, restTemplate, Role.USER);

        assertFalse(isSaveUser);
    }

    @Test
    @DisplayName("Создание пользователя в базе - ПРОВАЛ (Попытка сохранить дубликат User)")
    public void createUserDBFailureDuplicate() {
        User user = getUserWithoutId();

        boolean isSaveUser1 = createUser(user, restTemplate, Role.ADMIN);
        boolean isSaveUser2 = createUser(user, restTemplate, Role.ADMIN);

        assertTrue(isSaveUser1);
        assertFalse(isSaveUser2);
    }

    @Test
    @DisplayName("Создание пользователя в базе - ПРОВАЛ (Не валидные данные для сохранения")
    public void createUserDBFailureInvalidData() {
        User user = getUserWithoutId();
        user.setFirstName("12345");

        boolean isSaveUser = createUser(user, restTemplate, Role.ADMIN);

        assertFalse(isSaveUser);
    }

    @Test
    @DisplayName("Сохранение счета в базе - УСПЕХ (У авторизованного юзера достаточно прав)")
    void createAccountSuccessPermission() {
        Account account = getAccountWithoutId();

        boolean isSaveUser = createUser(account.getUser(), restTemplate, Role.ADMIN);
        boolean isSaveAccount = createAccount(account, restTemplate, Role.ADMIN);

        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertTrue(isSaveAccount)
        );
    }
    @Test
    @DisplayName("Сохранение счета в базе - ПРОВАЛ (У авторизованного юзера недостаточно прав)")
    void createAccountFailurePermissionDenied() {
        Account account = getAccountWithoutId();

        boolean isSaveUser = createUser(account.getUser(), restTemplate, Role.ADMIN);
        boolean isSaveAccount = createAccount(account, restTemplate, Role.USER);

        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertFalse(isSaveAccount)
        );
    }
    @Test
    @DisplayName("Сохранение счета в базе - ПРОВАЛ (Попытка сохранить дубликат Account)")
    void createAccountFailureDuplicate() {
        Account account = getAccountWithoutId();
        User user = account.getUser();

        boolean isSaveUser = createUser(user, restTemplate, Role.ADMIN);
        boolean isSaveAccount1 = createAccount(account, restTemplate, Role.ADMIN);
        boolean isSaveAccount2 = createAccount(account, restTemplate, Role.ADMIN);

        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertTrue(isSaveAccount1),
                () -> assertFalse(isSaveAccount2)
        );
    }

    @Test
    @DisplayName("Сохранение счета в базе - ПРОВАЛ (юзер не найден)")
    void createAccountFailure() {
        final long notExistUserId = Long.MAX_VALUE;
        Account account = getAccountWithoutId();
        account.getUser().setId(notExistUserId);

        User user = getUserWithoutId();

        boolean isSaveUser = createUser(user, restTemplate, Role.ADMIN);
        boolean isSaveAccount = createAccount(account, restTemplate, Role.ADMIN);

        assertAll(
                () -> assertTrue(isSaveUser),
                () -> assertFalse(isSaveAccount)
        );
    }
}
