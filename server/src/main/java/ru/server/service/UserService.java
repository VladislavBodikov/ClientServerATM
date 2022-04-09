package ru.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.server.entity.User;
import ru.server.repository.UserCrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private UserCrudRepository userCrudRepository;

    public Optional<User> save(User user) {
        boolean isUserExists = isExistsByFirstNameAndLastName(user);
        if (isUserExists) {
            return Optional.empty();
        }
        return Optional.of(userCrudRepository.save(user));
    }

    public int removeById(Long id){
        if (userCrudRepository.existsById(id)){
            return userCrudRepository.removeById(id);
        }
        return 0;
    }

    public int removeByFirstNameAndLastName(User user) {
        if (isExistsByFirstNameAndLastName(user)) {
            return userCrudRepository.removeByFirstNameAndLastName(user.getFirstName(), user.getLastName());
        }
        return 0;
    }

    public Optional<User> findById(Long id) {
        return userCrudRepository.findById(id);
    }

    public Optional<User> findByFirstNameAndLastName(User user) {
        return userCrudRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName());
    }

    public Optional<User> findByPassportData(String passportData) {
        return userCrudRepository.findByPassportData(passportData);
    }

    public Optional<User> findByNameIfNotHaveId(User user) {
        if (user.getId() != 0) {
            return findById(user.getId());
        }
        else if (user.getFirstName() != null && user.getLastName() != null) {
            return findByFirstNameAndLastName(user);
        }
        return Optional.empty();
    }

    public boolean isExistById(long id) {
        return findById(id).isPresent();
    }

    public boolean isExistsByFirstNameAndLastName(User user) {
        // temporary stub
        // filter to avoid duplicate by first_&_last name INSTEAD passport_data
        return userCrudRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName()).isPresent();
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        userCrudRepository.findAll().forEach(list::add);
        return list;
    }

}
