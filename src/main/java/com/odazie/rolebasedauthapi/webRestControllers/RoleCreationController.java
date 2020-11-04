package com.odazie.rolebasedauthapi.webRestControllers;

import com.odazie.rolebasedauthapi.business.service.UserService;
import com.odazie.rolebasedauthapi.data.entity.Role;
import com.odazie.rolebasedauthapi.data.entity.User;
import com.odazie.rolebasedauthapi.data.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleCreationController {
    private final UserService userService;
    private final RoleRepository roleRepository;


    public RoleCreationController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/roles")
    public ResponseEntity<Void> assignRole(@RequestBody Role role, Authentication authentication){
        User currentUser = userService.findUserByUsername(authentication.getName());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
