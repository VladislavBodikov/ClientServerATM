package ru.server.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.server.entity.Account;
import ru.server.entity.User;

import java.util.Optional;

import static ru.server.DataForUnitTests.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountRepoTest {

    @Autowired
    private AccountCrudRepository accountCrudRepository;
    @Autowired
    private UserCrudRepository userCrudRepository;

    @Test
    @DisplayName("SAVE and FIND account")
    void saveAndFindAccount() {
        Account account = getAccountWithSavedUser();

        accountCrudRepository.save(account);

        Optional<Account> accountFoundByCardNumber = accountCrudRepository.findByCardNumber(account.getCardNumber());
        Optional<Account> accountFoundByAccNumber = accountCrudRepository.findByScoreNumber(account.getScoreNumber());

        boolean isAccFoundByCardNumber = accountFoundByCardNumber.isPresent();
        boolean isAccFoundByAccNumber = accountFoundByAccNumber.isPresent();

        clearRepos();

        assertAll(
                () -> assertTrue(isAccFoundByCardNumber),
                () -> assertTrue(isAccFoundByAccNumber)
        );


    }

    @Test
    @DisplayName("SAVE and DELETE")
    void saveAndDeleteAccount(){
        Account account1 = getAccountWithSavedUser("1111000011110000","40804080408040804080");
        Account account2 = getAccountWithSavedUser("2222000022220000","40904090409040904090");

        accountCrudRepository.save(account1);
        accountCrudRepository.save(account2);

        int rowsAccRemovedByCardNumber = accountCrudRepository.removeByCardNumber("1111000011110000");
        int rowsAccRemovedByAccNumber = accountCrudRepository.removeByScoreNumber("40904090409040904090");

        boolean isAccFoundByCardNumber = rowsAccRemovedByCardNumber == 1;
        boolean isAccFoundByAccNumber = rowsAccRemovedByAccNumber == 1;

        clearRepos();

        assertAll(
                () -> assertTrue(isAccFoundByCardNumber),
                () -> assertTrue(isAccFoundByAccNumber)
        );
    }

    private Account getAccountWithSavedUser(){
        Account account = getAccountWithoutId();
        User savedUserFromDB = saveAndFindUser(account.getUser()).get();
        account.setUser(savedUserFromDB);
        return account;
    }
    private Account getAccountWithSavedUser(String cardNumber,String accountNumber){
        Account account = getAccountWithSavedUser();
        account.setCardNumber(cardNumber);
        account.setScoreNumber(accountNumber);
        return account;
    }

    private Optional<User> saveAndFindUser(User userToSave) {
        if (!userCrudRepository.findByFirstNameAndLastName(userToSave.getFirstName(),userToSave.getLastName()).isPresent()){
            userCrudRepository.save(userToSave);
        }
        return userCrudRepository.findByFirstNameAndLastName(userToSave.getFirstName(), userToSave.getLastName());
    }
    private void clearRepos(){
        userCrudRepository.deleteAll();
        accountCrudRepository.deleteAll();
    }

}
