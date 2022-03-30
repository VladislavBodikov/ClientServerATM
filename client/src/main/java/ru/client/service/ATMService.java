package ru.client.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.client.dto.BalanceDTO;

import java.util.Objects;

@Service
public class ATMService {

    public String showBalance(BalanceDTO balanceDTO) {
        HttpStatus balanceStatus = balanceDTO.getStatus();
        if (balanceStatus == null)
            return "Balance status is null";

        switch (balanceStatus) {
            case OK:
                return "\nCARD_NUMBER : " + balanceDTO.getCardNumber() + "\nBALANCE : " + balanceDTO.getAmount();
            case BAD_REQUEST:
                return "\nInvalid input data : check card_number and pin_code!\n";
            case EXPECTATION_FAILED:
                return "\nWRONG PIN-CODE\n";
            default:
                return "\nUnexpected HttpResponse status!!!\n";
        }
    }
}
