package ru.client.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.client.dto.BalanceDTO;

@Service
@AllArgsConstructor
public class ATMService {

    public String showBalance(BalanceDTO balanceDTO){
        if (balanceDTO.getAmount() == null){
            return "WRONG PIN-CODE";
        }
        return  "\nCARD_NUMBER : "   + balanceDTO.getCardNumber() +
                "\nBALANCE : "       + balanceDTO.getAmount();
    }
}
