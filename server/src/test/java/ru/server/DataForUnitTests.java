package ru.server;

import ru.server.entity.Account;
import ru.server.entity.User;

import java.math.BigDecimal;

public class DataForUnitTests {
    public static User getUserWithId() {
        User user = new User();
        user.setId(1);
        user.setFirstName("Vlad");
        user.setLastName("Bodik");
        return user;
    }

    public static Account getAccountWithId(){
        User user = getUserWithId();

        Account account = new Account();
        account.setId(1);
        account.setUser(user);
        account.setAmount(new BigDecimal("9999.01"));
        account.setCardNumber("1111000011110000");
        account.setScoreNumber("40804080408040804080");
        account.setPinCode("1111");

        return account;
    }

    public static User getUserWithoutId() {
        User user = new User();
        user.setFirstName("Vlad");
        user.setLastName("Bodik");

        return user;
    }

    public static Account getAccountWithoutId() {
        User user = getUserWithoutId();

        Account account = new Account();
        account.setUser(user);
        account.setAmount(new BigDecimal("9999.01"));
        account.setCardNumber("1111000011110000");
        account.setScoreNumber("40804080408040804080");
        account.setPinCode("1111");

        return account;
    }
}
