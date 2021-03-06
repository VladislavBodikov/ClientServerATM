package ru.server.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.server.model.entity.Account;
import ru.server.model.entity.User;

import java.util.Optional;

import static ru.server.DataForUnitTests.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountRepoTest {

    @Autowired
    private AccountCrudRepository accountCrudRepository;
    @Autowired
    private UserCrudRepository userCrudRepository;

    @AfterEach
    public void clearWasteDataAfterTests(){
        clearRepos(userCrudRepository,accountCrudRepository);
    }

    @Test
    @DisplayName("SAVE and FIND account")
    void saveAndFindAccount() {
        Account account = getAccountWithSavedUser();

        accountCrudRepository.save(account);

        Optional<Account> accountFoundByCardNumber = accountCrudRepository.findByCardNumber(account.getCardNumber());
        Optional<Account> accountFoundByAccNumber = accountCrudRepository.findByAccountNumber(account.getAccountNumber());

        boolean isAccFoundByCardNumber = accountFoundByCardNumber.isPresent();
        boolean isAccFoundByAccNumber = accountFoundByAccNumber.isPresent();

        assertAll(
                () -> assertTrue(isAccFoundByCardNumber),
                () -> assertTrue(isAccFoundByAccNumber)
        );
    }

    @Test
    @DisplayName("SAVE and DELETE")
    void saveAndDeleteAccount(){
        Account account1 = getAccountWithSavedUser("1111000011110000","44444444444444444444");
        Account account2 = getAccountWithSavedUser("2222000022220000","45555555555555555555");
        Account account3 = getAccountWithSavedUser("3333000033330000","46666666666666666666");

        accountCrudRepository.save(account1);
        accountCrudRepository.save(account2);
        accountCrudRepository.save(account3);
        long savedAccount3Id = accountCrudRepository.findByCardNumber("3333000033330000").get().getId();

        int rowsAccRemovedByCardNumber = accountCrudRepository.removeByCardNumber("1111000011110000");
        int rowsAccRemovedByAccNumber = accountCrudRepository.removeByAccountNumber("45555555555555555555");
        int rowsAccRemovedById = accountCrudRepository.removeById(savedAccount3Id);

        boolean isAccRemovedByCardNumber = rowsAccRemovedByCardNumber == 1;
        boolean isAccRemovedByAccNumber = rowsAccRemovedByAccNumber == 1;
        boolean isAccRemovedById = rowsAccRemovedById == 1;

        assertAll(
                () -> assertTrue(isAccRemovedByCardNumber),
                () -> assertTrue(isAccRemovedByAccNumber),
                () -> assertTrue(isAccRemovedById)
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
        account.setAccountNumber(accountNumber);
        return account;
    }

    private Optional<User> saveAndFindUser(User userToSave) {
        if (!userCrudRepository.findByFirstNameAndLastName(userToSave.getFirstName(),userToSave.getLastName()).isPresent()){
            userCrudRepository.save(userToSave);
        }
        return userCrudRepository.findByFirstNameAndLastName(userToSave.getFirstName(), userToSave.getLastName());
    }
}
