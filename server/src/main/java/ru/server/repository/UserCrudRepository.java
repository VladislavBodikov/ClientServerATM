package ru.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.server.model.entity.User;

import java.util.Optional;

@Repository
@Transactional
public interface UserCrudRepository extends CrudRepository<User, Long> {

    Optional<User> findByPassportData(String passportData);

    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);

    int removeByFirstNameAndLastName(String firstName, String lastName);

    int removeById(Long id);
}
