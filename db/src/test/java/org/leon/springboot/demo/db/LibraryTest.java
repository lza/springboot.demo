package org.leon.springboot.demo.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.leon.springboot.demo.db.dao.PermissionMapper;
import org.leon.springboot.demo.db.model.Permission;
import org.leon.springboot.demo.db.model.Role;
import org.leon.springboot.demo.db.model.User;
import org.leon.springboot.demo.db.service.RoleService;
import org.leon.springboot.demo.db.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by leon on 2017/4/25.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Library.class})
public class LibraryTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @Test
    public void test(){
        List<Permission> permissions = permissionMapper.getAll();
        for (Permission permission:permissions){
            printPermission(permission);
        }

        List<Role> roles = roleService.getRoles();
        for (Role role:roles){
            printRole(role);
        }

        List<User> users = userService.getAll();
        for (User user:users){
            printUser(user);
        }
    }

    private void printPermission(Permission permission){
        logger.debug("permission:{},{},{},{}", permission.getName(),
                permission.getDescription(), permission.getCreated(),
                permission.getLastModified());
    }

    private void printRole(Role role){
        logger.debug("role:{},{},{},{}", role.getName(),
                role.getDescription(),
                role.getCreated(), role.getLastModified());
        for (Permission permission:role.getPermissions()){
            printPermission(permission);
        }
    }
    private void printUser(User user){
        logger.debug("user:{},{},{},{}", user.getEmail(),
                user.getName(), user.getCreated(), user.getLastModified());
        for (Role role:user.getRoles()){
            printRole(role);
        }
    }
}
