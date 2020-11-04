package com.odazie.rolebasedauthapi.business.service;

import com.odazie.rolebasedauthapi.data.entity.User;
import com.odazie.rolebasedauthapi.security.MyUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UserService userService;


    public UserDetailsServiceImplementation(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserRepository().findByUsername(username);

        if (user == null){
            throw  new UsernameNotFoundException(username);
        }

        return new MyUserDetails(user);
    }



}
