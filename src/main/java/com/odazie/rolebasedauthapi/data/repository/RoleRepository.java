package com.odazie.rolebasedauthapi.data.repository;

import com.odazie.rolebasedauthapi.data.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}
