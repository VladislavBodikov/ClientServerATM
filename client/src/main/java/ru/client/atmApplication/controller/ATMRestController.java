package ru.client.atmApplication.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.client.atmApplication.entity.Score;

@RestController
@RequestMapping("/client")
@Slf4j
public class ATMRestController {

    final String hostUrl = "http://localhost:8082/host/balance";

    @PostMapping(path = "/balance", consumes = "application/json")
    public String balance(@RequestBody Score score) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Score> request = new HttpEntity<>(score);
        log.info(request.toString());
        ResponseEntity<Score> response = restTemplate.exchange(hostUrl, HttpMethod.POST, request, Score.class);
        log.info(response.toString());
        try {
            return "\n" + "BALANCE: \t" + response.getBody().getAmount();
        }
        catch (NullPointerException npe){
            return "Score not found";
        }
    }
}