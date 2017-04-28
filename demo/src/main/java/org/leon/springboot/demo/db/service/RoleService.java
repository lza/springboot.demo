package org.leon.springboot.demo.db.service;

import org.leon.springboot.demo.db.model.Role;

import java.util.List;

/**
 * Created by leon on 2017/4/26.
 */
public interface RoleService {
    void insert(Role role);
    void delete(Role role);
    void update(Role role);
    List<Role> getRoles();
    void deleteAll();
    Role findByName(String name);
    Role findById(Long id);
}
