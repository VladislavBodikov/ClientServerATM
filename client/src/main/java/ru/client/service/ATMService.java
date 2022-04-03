package ru.client.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.client.dto.BalanceDTO;

import java.math.BigDecimal;

@Service
public class ATMService {

    public String printBalanceResponse(ResponseEntity<BalanceDTO> response) {
        if (response.getBody() == null){
            return  "ERROR : Response.body == null";
        }

        BalanceDTO balanceDTO = response.getBody();
        HttpStatus balanceStatus = balanceDTO.getStatus();

        if (balanceStatus == null){
            return "Balance status is null";
        }

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

    public String printResultOfTransaction(
            ResponseEntity<BalanceDTO> balanceBeforeTransfer,
            ResponseEntity<BalanceDTO> balanceAfterTransfer,
            BigDecimal amountToTransfer){

        BigDecimal before = balanceBeforeTransfer.getBody().getAmount();
        BigDecimal after = balanceAfterTransfer.getBody().getAmount();

        if (before.subtract(amountToTransfer).compareTo(after) == 0){
            return "\nTransfer success!\n" +
                    "Balance BEFORE: " + before + "\n" +
                    "Balance AFTER: " + after + "\n";
        }
        return "\nTransfer denied!\n";
    }
}
