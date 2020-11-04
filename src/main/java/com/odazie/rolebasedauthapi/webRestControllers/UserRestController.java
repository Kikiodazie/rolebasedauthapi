package com.odazie.rolebasedauthapi.webRestControllers;

import com.odazie.rolebasedauthapi.business.service.UserService;
import com.odazie.rolebasedauthapi.data.entity.Role;
import com.odazie.rolebasedauthapi.data.entity.User;
import com.odazie.rolebasedauthapi.data.repository.RoleRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class UserRestController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserRestController(UserService userService, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }



    @PostMapping("/signup")
    public ResponseEntity<Void> userSignUp(@RequestBody User user, UriComponentsBuilder builder){
        if (userService.getUserRepository().findByUsername(user.getUsername()) != null){
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        HttpHeaders headers = new HttpHeaders();

        headers.setLocation(builder.path("/users/{userId}").buildAndExpand(user.getId()).toUri());

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addrole")
    public ResponseEntity<Void> assignRole(@RequestBody Role role, Authentication authentication){
        User currentUser = userService.findUserByUsername(authentication.getName());
        currentUser.getRoles().add(role);
        roleRepository.save(role);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
