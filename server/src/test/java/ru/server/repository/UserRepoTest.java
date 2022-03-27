package ru.server.repository;

import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.server.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.server.DataForUnitTests.getUserWithoutId;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class UserRepoTest {

    @Autowired
    private UserCrudRepository userCrudRepository;

    @Test
    @DisplayName("SAVE and FIND user")
    void saveAndFind(){
        User user = getUserWithoutId();
        user.setPassportData("4018198815");
        User saved = userCrudRepository.save(user);

        Optional<User> userFromDbByName = userCrudRepository.findByFirstNameAndLastName(user.getFirstName(),user.getLastName());
        Optional<User> userFromDbById = userCrudRepository.findById(userFromDbByName.get().getId());
        Optional<User> userFromDbByPassport = userCrudRepository.findByPassportData(user.getPassportData());

        List<User> users = new ArrayList<>();
        userCrudRepository.findAll().forEach(users::add);

        clearRepo();

        assertAll(
                ()->assertNotNull(saved),
                ()->assertTrue(userFromDbByName.isPresent()),
                ()->assertTrue(userFromDbById.isPresent()),
                ()->assertTrue(userFromDbByPassport.isPresent()),
                ()->assertEquals(user, users.get(0))
        );
    }

    @Test
    @DisplayName("SAVE and DELETE - by entity")
    void saveAndDelete(){
        User user = getUserWithoutId();
        User saved = userCrudRepository.save(user);

        userCrudRepository.delete(user);

        Optional<User> deletedUser = userCrudRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName());

        clearRepo();

        assertAll(
                ()->assertNotNull(saved),
                ()->assertFalse(deletedUser.isPresent())
        );
    }

    @Test
    @DisplayName("SAVE and DELETE - by name")
    void saveAndDeleteByName(){
        User user = getUserWithoutId();
        User saved = userCrudRepository.save(user);

        int rows = userCrudRepository.removeByFirstNameAndLastName(user.getFirstName(), user.getLastName());

        Optional<User> deletedUser = userCrudRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName());

        clearRepo();

        assertAll(
                ()->assertNotNull(saved),
                ()->assertEquals(1, rows),
                ()->assertFalse(deletedUser.isPresent())
        );
    }
    private void clearRepo(){
        userCrudRepository.deleteAll();
    }
}
