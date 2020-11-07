package com.odazie.rolebasedauthapi.business.service;

import com.odazie.rolebasedauthapi.data.entity.Role;
import com.odazie.rolebasedauthapi.data.entity.User;
import com.odazie.rolebasedauthapi.data.repository.RoleRepository;
import com.odazie.rolebasedauthapi.data.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;



    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User findUserByUsername(String username){

        return getUserRepository().findByUsername(username);
    }
    public void saveUser(User user ){

        Role role = roleRepository.findByName("USER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        if(user.getUsername().equals("adminTEST")){
           role = roleRepository.findByName("ADMIN");
           roles.add(role);
        }

        user.setRoles(roles);
        getUserRepository().save(user);
    }

    public User findUserById(Long userId){
        return getUserRepository().findUserById(userId);
    }

    public void deleteUser(User user){
        getUserRepository().delete(user);
    }


    public UserRepository getUserRepository() {
        return userRepository;
    }
}
