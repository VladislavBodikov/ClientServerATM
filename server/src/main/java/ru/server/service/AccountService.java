package ru.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.server.entity.Account;
import ru.server.repository.AccountCrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AccountService {

    private AccountCrudRepository accountCrudRepository;

    public Optional<Account> save(Account account) {
        boolean isScoreNumberExists = findByScoreNumber(account.getScoreNumber()).isPresent();
        boolean isCardNumberExists = findByCardNumber(account.getCardNumber()).isPresent();

        if (isCardNumberExists || isScoreNumberExists){
            return Optional.empty();
        }
        return Optional.of(accountCrudRepository.save(account));
    }

    public List<Account> getAllScores() {
        List<Account> list = new ArrayList<>();
        accountCrudRepository.findAll().forEach(list::add);
        return list;
    }

    public Optional<Account> findByCardNumber(String cardNumber) {
        return accountCrudRepository.findByCardNumber(cardNumber);
    }

    public Optional<Account> findByScoreNumber(String scoreNumber) {
        return accountCrudRepository.findByScoreNumber(scoreNumber);
    }

    public int removeByCardNumber(String cardNumber){
        if (isCardNumberExist(cardNumber)){
            return accountCrudRepository.removeByCardNumber(cardNumber);
        }
        return 0;
    }

    public int removeByScoreNumber(String scoreNumber){
        if (isScoreNumberExist(scoreNumber)) {
            return accountCrudRepository.removeByScoreNumber(scoreNumber);
        }
        return 0;
    }

    public boolean isScoreNumberExist(String scoreNumber){
        return findByScoreNumber(scoreNumber).isPresent();
    }

    public boolean isCardNumberExist(String cardNumber){
        return findByCardNumber(cardNumber).isPresent();
    }
}
