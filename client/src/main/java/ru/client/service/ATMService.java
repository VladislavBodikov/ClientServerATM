package ru.client.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.client.dto.BalanceDTO;

import java.math.BigDecimal;

@Service
public class ATMService {

    public String printBalanceResponse(ResponseEntity<BalanceDTO> response) {
        if (response == null)
            return "ERROR : response == null / check connection with server!";

        if (response.getBody() != null){
            BalanceDTO body = response.getBody();
            if (body.getMessage() != null) {
                return "\n" + body.getMessage() + "\n";
            } else {
                return "\nCARD_NUMBER : " + body.getCardNumber() + "\nBALANCE : " + body.getAmount();
            }
        }
        return "Response body == null";
    }

    public String printResultOfTransaction(ResponseEntity<BalanceDTO> response) {
        if (response == null)
            return "ERROR : response == null / check connection with server!";

        if (response.getBody() != null){
            BalanceDTO body = response.getBody();
            if (body.getMessage() != null) {
                return "\n" + body.getMessage() + "\n";
            } else {
                return "\nTransfer success!\n";
            }
        }
        return "Response body == null";
    }

}
