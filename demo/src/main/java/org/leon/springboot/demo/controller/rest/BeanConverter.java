package org.leon.springboot.demo.controller.rest;

import org.leon.springboot.demo.db.dao.PermissionMapper;
import org.leon.springboot.demo.db.model.Permission;
import org.leon.springboot.demo.db.model.Role;
import org.leon.springboot.demo.db.model.User;
import org.leon.springboot.demo.db.service.RoleService;
import org.leon.springboot.demo.db.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by leon on 2017/4/27.
 */
@Component
public class BeanConverter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Bean
    public Converter<String, User> userConverter(){
        return new Converter<String, User>(){

            @Override
            public User convert(String email) {
                return userService.findByEmailAndActive(email, true);
            }
        };
    }

    @Bean
    public Converter<String, Role> roleConverter(){
        return new Converter<String, Role>(){

            @Override
            public Role convert(String id) {
                return roleService.findById(Long.parseLong(id));
            }
        };
    }

    @Bean
    public Converter<String, Permission> permissionConverter(){
        return new Converter<String, Permission>(){

            @Override
            public Permission convert(String id) {
                return permissionMapper.selectByPrimaryKey(Long.parseLong(id));
            }
        };
    }
}
