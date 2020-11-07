package com.odazie.rolebasedauthapi.webRestControllers;

import com.odazie.rolebasedauthapi.business.model.AuthToken;
import com.odazie.rolebasedauthapi.business.service.UserService;
import com.odazie.rolebasedauthapi.data.entity.Role;
import com.odazie.rolebasedauthapi.data.entity.User;
import com.odazie.rolebasedauthapi.data.repository.RoleRepository;
import com.odazie.rolebasedauthapi.securityConfig.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserRestController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;


    public UserRestController(UserService userService, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }



    @PostMapping("/signup")
    public ResponseEntity<Void> userSignUp(@RequestBody User user){
        if (userService.getUserRepository().findByUsername(user.getUsername()) != null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return new ResponseEntity<>( HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@RequestBody User currentUser) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        currentUser.getUsername(),
                        currentUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new AuthToken(token));
    }






    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/role/{userId}")
    public ResponseEntity<Void> assignRole(@RequestBody Role role, @PathVariable Long userId){
        User user = userService.findUserById(userId);
        user.getRoles().add(role);
        roleRepository.save(role);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        User user = userService.findUserById(userId);
        if (user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //userService.deleteUser(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
