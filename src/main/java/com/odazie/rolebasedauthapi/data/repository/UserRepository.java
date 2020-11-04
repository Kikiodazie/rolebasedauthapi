package com.odazie.rolebasedauthapi.data.repository;

import com.odazie.rolebasedauthapi.data.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);


}
