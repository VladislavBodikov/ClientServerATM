package ru.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.server.model.entity.Account;

import java.util.Optional;

@Repository
@Transactional
public interface AccountCrudRepository extends CrudRepository<Account, Long> {

    Optional<Account> findByCardNumber(String cardNumber);

    Optional<Account> findByAccountNumber(String scoreNumber);

    int removeByCardNumber(String cardNumber);

    int removeByAccountNumber(String scoreNumber);

    int removeById(Long id);
}
