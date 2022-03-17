package ru.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.server.entity.Account;

import java.util.Optional;

@Repository
public interface AccountCrudRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByCardNumber(String cardNumber);
    Optional<Account> findByScoreNumber(String scoreNumber);
}
