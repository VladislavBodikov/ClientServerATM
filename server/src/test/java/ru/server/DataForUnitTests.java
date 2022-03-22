package ru.server;

import ru.server.entity.Account;
import ru.server.entity.User;

import java.math.BigDecimal;

public class DataForUnitTests {
    public static User getUser() {
        User user = new User();
        user.setId(1);
        user.setFirstName("Vlad");
        user.setLastName("Bodik");
        return user;
    }

    public static Account getAccount(){
        User user = getUser();

        Account account = new Account();
        account.setId(1);
        account.setUser(user);
        account.setAmount(new BigDecimal("9999.01"));
        account.setCardNumber("1234");
        account.setScoreNumber("4321");
        account.setPinCode("1111");

        return account;
    }
}
