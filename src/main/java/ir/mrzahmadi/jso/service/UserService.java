package ir.mrzahmadi.jso.service;

import ir.mrzahmadi.jso.model.User;
import ir.mrzahmadi.jso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public User registerUser(User user) {
        return userRepository.save(user);
    }

}
