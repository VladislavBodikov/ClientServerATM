package ru.server.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import ru.server.exeption.AccountNotFoundException;
import ru.server.exeption.DontHaveEnoughMoneyException;

import java.util.function.Function;

public class CustomExceptionsUnitTest {
    
    @Test
    void accountNotFound(){
        AccountNotFoundException accountNotFoundException =
                new AccountNotFoundException("acc not found",new AccountNotFoundException("acc not found"));
        assertEquals("acc not found",accountNotFoundException.getMessage());
        assertThrows(AccountNotFoundException.class,()->{throw accountNotFoundException;});

    }
    @Test
    void dontHaveEnoughMoney(){
        DontHaveEnoughMoneyException dontHaveEnoughMoneyException =
                new DontHaveEnoughMoneyException("has not money",new DontHaveEnoughMoneyException("has not money"));
        assertEquals("has not money",dontHaveEnoughMoneyException.getMessage());
        assertThrows(DontHaveEnoughMoneyException.class,()->{throw dontHaveEnoughMoneyException;});

    }
}
