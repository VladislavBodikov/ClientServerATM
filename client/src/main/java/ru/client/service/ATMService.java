package ru.client.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.client.dto.BalanceDTO;

@Service
public class ATMService {

    public String showBalance(BalanceDTO balanceDTO) {
        HttpStatus balanceStatus = balanceDTO.getStatus();

        if (balanceStatus == HttpStatus.OK) {
            return "\nCARD_NUMBER : " + balanceDTO.getCardNumber() +
                    "\nBALANCE : " + balanceDTO.getAmount();
        } else if (balanceStatus == HttpStatus.BAD_REQUEST) {
            return "\nInvalid input data : check card_number and pin_code!\n";
        } else if (balanceStatus == HttpStatus.EXPECTATION_FAILED) {
            return "\nWRONG PIN-CODE\n";
        } else return "\nUnexpected HttpResponse status!!!\n";
    }
}
