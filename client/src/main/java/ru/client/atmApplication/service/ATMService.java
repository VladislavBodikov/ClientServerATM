package ru.client.atmApplication.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.client.atmApplication.entity.Account;
import ru.client.dto.BalanceDTO;

@Service
@AllArgsConstructor
public class ATMService {

    public String showBalance(BalanceDTO balanceDTO){
        if (balanceDTO.getAmount() == null){
            return "Score not found";
        }
        return  "\nCARD_NUMBER : "   + balanceDTO.getCardNumber() +
                "\nBALANCE : "       + balanceDTO.getAmount();
    }
}
