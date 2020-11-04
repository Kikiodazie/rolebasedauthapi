package com.odazie.rolebasedauthapi.business.service;

import com.odazie.rolebasedauthapi.data.entity.User;
import com.odazie.rolebasedauthapi.data.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByUsername(String username){

        return getUserRepository().findByUsername(username);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
