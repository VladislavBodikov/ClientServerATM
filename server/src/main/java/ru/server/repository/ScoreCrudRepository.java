package ru.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.server.entity.Score;
import ru.server.entity.User;

import java.util.Optional;

@Repository
public interface ScoreCrudRepository extends CrudRepository<Score, Long> {
    Optional<Score> findByCardNumber(String cardNumber);
}
