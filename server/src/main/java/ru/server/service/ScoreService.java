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
public class ScoreService {

    private AccountCrudRepository accountCrudRepository;

    public List<Account> getAllScores() {
        List<Account> list = new ArrayList<>();
        accountCrudRepository.findAll().forEach(list::add);
        return list;
    }

    public Account save(Account account) {
        return accountCrudRepository.save(account);
    }

    public Optional<Account> findByCardNumber(String cardNumber) {
        return accountCrudRepository.findByCardNumber(cardNumber);
    }
}
