package org.leon.springboot.demo.db.service;

import org.leon.springboot.demo.db.model.User;

import java.util.List;

/**
 * Created by leon on 2017/4/26.
 */
public interface UserService {
    void insert(User user);
    void delete(User user);
    void update(User user);
    List<User> getAll();
    void deleteAll();
    User findByEmailAndActive(String email, Boolean active);
}
