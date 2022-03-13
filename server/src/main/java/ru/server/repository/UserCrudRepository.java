package ru.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.server.entity.User;

import java.util.Optional;

@Repository
public interface UserCrudRepository extends CrudRepository<User,Long> {

    Optional<User> findByPassportData(String passportData);
}
