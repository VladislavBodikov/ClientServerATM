package ru.server.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.server.entity.User;
import ru.server.repository.UserCrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.server.DataForUnitTests.getUser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnitUserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserCrudRepository userCrudRepository;

    @Test
    @DisplayName("SAVE - failure ( user already exist)")
    void saveUserFailure(){
        User user = getUser();
        Mockito.when(userCrudRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName()))
                .thenReturn(Optional.of(user));
        Optional<User> optUser = userService.save(user);
        boolean isSaved = optUser.isPresent();

        assertFalse(isSaved);
    }
    @Test
    @DisplayName("SAVE - success")
    void saveUserSuccess(){
        User user = getUser();
        Mockito.when(userCrudRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName()))
                .thenReturn(Optional.empty());
        Mockito.when(userCrudRepository.save(Mockito.any()))
                .thenReturn(user);

        Optional<User> optUser = userService.save(user);
        boolean isSaved = optUser.isPresent();

        assertTrue(isSaved);
    }
    @Test
    @DisplayName("REMOVE - by name - SUCCESS")
    void removeByNameSuccess(){
        User user = getUser();
        Mockito.when(userCrudRepository.findByFirstNameAndLastName(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.of(user));
        Mockito.when(userCrudRepository.removeByFirstNameAndLastName(user.getFirstName(), user.getLastName()))
                .thenReturn(1);
        boolean someRowWasChangedInDB = userService.removeByFirstNameAndLastName(user) == 1;

        assertTrue(someRowWasChangedInDB);
    }
    @Test
    @DisplayName("REMOVE - by name - FAILURE")
    void removeByNameFailure(){
        User user = getUser();
        Mockito.when(userCrudRepository.removeByFirstNameAndLastName(user.getFirstName(), user.getLastName()))
                .thenReturn(0);
        boolean someRowWasChangedInDB = userService.removeByFirstNameAndLastName(user) > 0;

        assertFalse(someRowWasChangedInDB);
    }
    @Test
    @DisplayName("Find by ID - failure")
    void findByIdFailure(){

        User user = getUser();
        Mockito.when(userCrudRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        boolean isUserFound = userService.findById(user.getId()).isPresent();

        assertFalse(isUserFound);
    }
    @Test
    @DisplayName("Find by ID - success")
    void findByIdSuccess(){
        User user = getUser();
        Mockito.when(userCrudRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(user));
        boolean isUserFound = userService.findById(user.getId()).isPresent();

        assertTrue(isUserFound);
    }
    @Test
    @DisplayName("Find by NAME - failure")
    void findByNameFailure(){
        User user = getUser();
        Mockito.when(userCrudRepository.findByFirstNameAndLastName(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.empty());
        boolean isUserFound = userService.findByFirstNameAndLastName(user).isPresent();

        assertFalse(isUserFound);
    }
    @Test
    @DisplayName("Find by NAME - success")
    void findByNameSuccess(){
        User user = getUser();
        Mockito.when(userCrudRepository.findByFirstNameAndLastName(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.of(user));
        boolean isUserFound = userService.findByFirstNameAndLastName(user).isPresent();

        assertTrue(isUserFound);
    }
    @Test
    @DisplayName("Find by PASSPORT - failure")
    void findByPassportFailure(){
        User user = getUser();
        Mockito.when(userCrudRepository.findByPassportData(Mockito.any()))
                .thenReturn(Optional.empty());
        boolean isUserFound = userService.findByPassportData(user.getPassportData()).isPresent();

        assertFalse(isUserFound);
    }
    @Test
    @DisplayName("Find by PASSPORT - success")
    void findByPassportSuccess(){
        User user = getUser();
        Mockito.when(userCrudRepository.findByPassportData(Mockito.any()))
                .thenReturn(Optional.of(user));
        boolean isUserFound = userService.findByPassportData(user.getPassportData()).isPresent();

        assertTrue(isUserFound);
    }
    @Test
    @DisplayName("Find by NAME OR ID - failure")
    void findByNameOfNotHaveIdFailure(){
        User user = getUser();
        user.setId(0);
        user.setFirstName(null);
        user.setLastName(null);
        boolean isUserFound = userService.findByNameIfNotHaveId(user).isPresent();

        assertFalse(isUserFound);
    }
    @Test
    @DisplayName("Find by NAME OR ID - success")
    void findByNameOfNotHaveIdSuccessById(){
        User user = getUser();
        user.setId(1);
        Mockito.when(userCrudRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(user));
        boolean isUserFound = userService.findByNameIfNotHaveId(user).isPresent();

        assertTrue(isUserFound);
    }
    @Test
    @DisplayName("Find by NAME OR ID - success")
    void findByNameOfNotHaveIdSuccessByName(){
        User user = getUser();
        user.setId(0);
        Mockito.when(userCrudRepository.findByFirstNameAndLastName(Mockito.any(),Mockito.any()))
                .thenReturn(Optional.of(user));
        boolean isUserFound = userService.findByNameIfNotHaveId(user).isPresent();

        assertTrue(isUserFound);
    }
    @Test
    @DisplayName("Exist by ID - failure")
    void isExistByIdFailure(){
        User user = getUser();
        Mockito.when(userCrudRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        boolean isUserExist = userService.isExistById(user.getId());

        assertFalse(isUserExist);
    }
    @Test
    @DisplayName("Exist by ID - success")
    void isExistByIdSuccess(){
        User user = getUser();
        Mockito.when(userCrudRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(user));
        boolean isUserExist = userService.isExistById(user.getId());

        assertTrue(isUserExist);
    }
    @Test
    @DisplayName("get all users")
    void getAllUsers(){
        User user1 = getUser();
        user1.setId(2);
        User user2 = getUser();
        user1.setId(4);
        Mockito.when(userCrudRepository.findAll())
                .thenReturn(new ArrayList<User>(){{add(user1);add(user2);}});
        List<User> usersFromRepo = userService.getAllUsers();
        boolean containUsersWithId2And4 = usersFromRepo.contains(user1)
                                        && usersFromRepo.contains(user2);

        assertTrue(containUsersWithId2And4);
    }

}
