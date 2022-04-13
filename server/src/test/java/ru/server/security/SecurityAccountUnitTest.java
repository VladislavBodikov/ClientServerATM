package ru.server.security;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.server.DataForUnitTests;
import ru.server.entity.Account;
import ru.server.entity.User;

public class SecurityAccountUnitTest {

    private static Account account;
    private static SecurityAccount securityAccount;

    @BeforeAll
    static void createSecurityAccount() {
        account = DataForUnitTests.getAccountWithoutId();
        securityAccount = new SecurityAccount(account, true);
        securityAccount.setAccount(account);
        securityAccount.setActive(true);
    }

    @Test
    void gettersCheck(){
        User user = account.getUser();
        securityAccount.getUsername();
        assertAll(
                ()->assertEquals(account,securityAccount.getAccount()),
                ()->assertEquals(user.getRole().getAuthorities(),securityAccount.getAuthorities()),
                ()->assertEquals(account.getCardNumber(),securityAccount.getUsername()),
                ()->assertEquals(account.getPinCode(),securityAccount.getPassword()),
                ()->assertTrue(securityAccount.isActive()),
                ()->assertTrue(securityAccount.isAccountNonExpired()),
                ()->assertTrue(securityAccount.isAccountNonLocked()),
                ()->assertTrue(securityAccount.isEnabled()),
                ()->assertTrue(securityAccount.isCredentialsNonExpired())
        );
    }
}
