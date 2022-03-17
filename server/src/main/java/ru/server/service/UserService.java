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

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        userCrudRepository.findAll().forEach(list::add);
        return list;
    }

    public Optional<User> findById(Long id) {
        return userCrudRepository.findById(id);
    }

    public Optional<User> save(User user) {
        boolean isUserExists = isUserExistsByFirstNameAndLastName(user);
        if (isUserExists){
            return Optional.empty();
        }
        return Optional.of(userCrudRepository.save(user));
    }

    public int removeByFirstNameAndLastName(User user){
        if (isUserExistsByFirstNameAndLastName(user)){
            return userCrudRepository.removeByFirstNameAndLastName(user.getFirstName(), user.getLastName());
        }
        return 0;
    }

    public boolean isUserExistById(long id){
        return findById(id).isPresent();
    }

    public boolean isUserExistsByFirstNameAndLastName(User user){
        // temporary stub
        // filter to avoid duplicate by first_&_last name INSTEAD passport_data
        return userCrudRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName()).isPresent();
    }

    public Optional<User> findByPassportData(String passportData){
        return userCrudRepository.findByPassportData(passportData);
    }

}
