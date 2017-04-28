package org.leon.springboot.demo.db.service.impl;

import org.leon.springboot.demo.db.dao.UserMapper;
import org.leon.springboot.demo.db.dao.UserRolesMapper;
import org.leon.springboot.demo.db.model.Role;
import org.leon.springboot.demo.db.model.User;
import org.leon.springboot.demo.db.model.UserRoles;
import org.leon.springboot.demo.db.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by leon on 2017/4/26.
 */
@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRolesMapper userRolesMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(User user) {
        userMapper.insertSelective(user);
        updateUserRole(user);
    }

    private void updateUserRole(User user){
        List<Role> roles = user.getRoles();
        if(roles != null){
            userRolesMapper.deleteByUserId(user.getId());
            for (Role role:user.getRoles()){
                UserRoles userRoles = new UserRoles();
                userRoles.setUserId(user.getId());
                userRoles.setRolesId(role.getId());
                try {
                    userRolesMapper.insert(userRoles);
                } catch (DuplicateKeyException e){
                    logger.warn("exception:" + e);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(User user) {
        userMapper.deleteByPrimaryKey(user.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
        updateUserRole(user);
    }

    @Override
    public List<User> getAll() {
        return userMapper.getAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll() {
        userMapper.deleteAll();
    }

    @Override
    public User findByEmailAndActive(String email, Boolean active) {
        return userMapper.findByEmailAndActive(email, active);
    }
}
