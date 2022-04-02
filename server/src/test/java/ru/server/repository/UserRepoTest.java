package ru.server.repository;

import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import ru.server.DataForUnitTests;
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

    private List<CrudRepository> listReposForClear;

    @Test
    @DisplayName("SAVE and FIND user")
    void saveAndFind() {
        User user = getUserWithoutId();
        user.setPassportData("4018198815");
        User saved = userCrudRepository.save(user);

        Optional<User> userFromDbByName = userCrudRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName());
        Optional<User> userFromDbById = userCrudRepository.findById(userFromDbByName.get().getId());
        Optional<User> userFromDbByPassport = userCrudRepository.findByPassportData(user.getPassportData());

        List<User> usersFromRepo = new ArrayList<>();
        userCrudRepository.findAll().forEach(usersFromRepo::add);

        clearRepos();

        assertAll(
                () -> assertNotNull(saved),
                () -> assertTrue(userFromDbByName.isPresent()),
                () -> assertTrue(userFromDbById.isPresent()),
                () -> assertTrue(userFromDbByPassport.isPresent()),
                () -> assertEquals(user, usersFromRepo.get(0))
        );
    }

//    @Test
//    @DisplayName("SAVE and DELETE - by entity (extract first_name and last_name from input User")
//    void saveAndDelete() {
//        User user = getUserWithoutId();
//        User saved = userCrudRepository.save(user);
//
//        userCrudRepository.delete(user);
//
//        Optional<User> deletedUser = userCrudRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName());
//
//        clearRepos();
//
//        assertAll(
//                () -> assertNotNull(saved),
//                () -> assertFalse(deletedUser.isPresent())
//        );
//    }

    @Test
    @DisplayName("SAVE and DELETE - by name")
    void saveAndDeleteByName() {
        User user = getUserWithoutId();
        User saved = userCrudRepository.save(user);

        int rows = userCrudRepository.removeByFirstNameAndLastName(user.getFirstName(), user.getLastName());

        Optional<User> deletedUser = userCrudRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName());

        clearRepos();

        assertAll(
                () -> assertNotNull(saved),
                () -> assertEquals(1, rows),
                () -> assertFalse(deletedUser.isPresent())
        );
    }

    @Test
    @DisplayName("SAVE and DELETE - by ID")
    void saveAndDeleteById() {
        User user = getUserWithoutId();
        User saved = userCrudRepository.save(user);

        int rows = userCrudRepository.removeById(saved.getId());

        Optional<User> deletedUser = userCrudRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName());

        clearRepos();

        assertAll(
                () -> assertNotNull(saved),
                () -> assertEquals(1, rows),
                () -> assertFalse(deletedUser.isPresent())
        );
    }

    private void clearRepos() {
        initListReposForClear();
        DataForUnitTests.clearRepos(listReposForClear);
    }

    private List<CrudRepository> initListReposForClear() {
        if (listReposForClear == null) {
            listReposForClear = new ArrayList<>() {{
                add(userCrudRepository);
            }};
        }
        return listReposForClear;
    }

}
