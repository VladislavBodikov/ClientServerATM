package ru.client.atmApplication.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.client.atmApplication.entity.Account;

@Service
@AllArgsConstructor
public class ATMService {

    public String showBalance(Account account){
        if (account.getAmount() == null){
            return "Score not found";
        }
        return  "\nCARD_NUMBER : "   + account.getCardNumber() +
                "\nBALANCE : "       + account.getAmount();
    }
}
