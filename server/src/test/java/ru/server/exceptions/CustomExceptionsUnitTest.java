package ru.server.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

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
    @Test
    void negativeAmount(){
        NegativeAmountToTransferException negativeAmountToTransferException =
                new NegativeAmountToTransferException("negative amount",new NegativeAmountToTransferException("negative amount"));
        assertEquals("negative amount",negativeAmountToTransferException.getMessage());
        assertThrows(NegativeAmountToTransferException.class,()->{throw negativeAmountToTransferException;});
    }
    @Test
    void selfCardTransfer(){
        SendMoneyToSelfCardException sendMoneyToSelfCardException =
                new SendMoneyToSelfCardException("self-card",new NegativeAmountToTransferException("self-card"));
        assertEquals("self-card",sendMoneyToSelfCardException.getMessage());
        assertThrows(SendMoneyToSelfCardException.class,()->{throw sendMoneyToSelfCardException;});
    }
}
