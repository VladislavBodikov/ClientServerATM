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

    public User save(User user) {
        return userCrudRepository.save(user);
    }

    public boolean isUserExistById(long id){
        return findById(id).isPresent();
    }

    public Optional<User> findByPassportData(String passportData){
        return userCrudRepository.findByPassportData(passportData);
    }

}
