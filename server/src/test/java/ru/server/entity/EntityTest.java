package ru.server.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class EntityTest {

    @Test
    void accountTest(){
        Account account = initAccount();
        User user = account.getUser();
        assertAll(
//                ()->assertFalse(isNull),
                ()->assertEquals(1,account.getId()),
                ()->assertEquals(new BigDecimal("99.5"),account.getAmount()),
                ()->assertEquals("4321",account.getScoreNumber()),
                ()->assertEquals("1234",account.getCardNumber()),
                ()->assertEquals("1111",account.getPinCode()),
                ()->assertEquals(1,user.getId()),
                ()->assertEquals("Vlad",user.getFirstName()),
                ()->assertEquals("Bodik",user.getLastName()),
                ()->assertEquals("4518",user.getPassportData()),
                ()->assertEquals(new HashSet<>(),user.getAccounts()),
                ()->assertEquals("Account(id=1, cardNumber=1234, scoreNumber=4321, amount=99.5, pinCode=****)",account.toString()),
                ()->assertEquals("User(id=1, firstName=Vlad, lastName=Bodik, passportData=4518)",user.toString())
//                ()->
        );

    }
    private User initUser(){
        User user = new User();
        user.setFirstName("Vlad");
        user.setLastName("Bodik");
        user.setId(1);
        user.setPassportData("4518");
        Set<Account> accounts = new HashSet<>();
        user.setAccounts(accounts);

        return user;
    }
    private Account initAccount(){
        User user = initUser();

        Account account = new Account();
        account.setUser(user);
        account.setAmount(new BigDecimal("99.5"));
        account.setCardNumber("1234");
        account.setScoreNumber("4321");
        account.setPinCode("1111");
        account.setId(1);

        return account;
    }
}
