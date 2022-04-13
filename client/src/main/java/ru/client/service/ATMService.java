package ru.client.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.client.dto.BalanceDTO;

import java.math.BigDecimal;

@Service
public class ATMService {

    private final String RESPONSE_FROM_SERVER_IS_NULL = "\nERROR : response == null. Check connection with server!\n";
    private final String RESPONSE_BODY_FROM_SERVER_IS_NULL = "\nERROR : ERROR : Response.body == null\n";

    public String printBalanceResponse(ResponseEntity<BalanceDTO> response) {
        if (response == null) {
            return RESPONSE_FROM_SERVER_IS_NULL;
        }
        if (response.getBody() == null) {
            return RESPONSE_BODY_FROM_SERVER_IS_NULL;
        }

        BalanceDTO body = response.getBody();
        if (body.getMessage() != null) {
            return "\n" + body.getMessage() + "\n";
        } else {
            return "\nCARD_NUMBER : " + body.getCardNumber() + "\nBALANCE : " + body.getAmount();
        }
    }

    public String printResultOfTransaction(ResponseEntity<BalanceDTO> response) {
        if (response == null) {
            return RESPONSE_FROM_SERVER_IS_NULL;
        }
        if (response.getBody() == null) {
            return RESPONSE_BODY_FROM_SERVER_IS_NULL;
        }

        BalanceDTO body = response.getBody();
        if (body.getMessage() != null) {
            return "\n" + body.getMessage() + "\n";
        } else {
            return "\nTransfer success!\n";
        }
    }

}
