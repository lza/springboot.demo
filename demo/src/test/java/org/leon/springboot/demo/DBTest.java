package org.leon.springboot.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.leon.springboot.demo.db.dao.RoleMapper;
import org.leon.springboot.demo.db.dao.UserMapper;
import org.leon.springboot.demo.db.model.Permission;
import org.leon.springboot.demo.db.model.Role;
import org.leon.springboot.demo.db.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by leon on 2017/4/25.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DBTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void test(){
        Role role = roleMapper.findById(5L);
        logger.debug("role:{}", role.getName());
        for (Permission permission:role.getPermissions()){
            logger.debug("permission:{}", permission.getName());
        }
        User user = userMapper.findByEmailAndActive("admin@cgtn.com", true);
        logger.debug("user:{}", user.getEmail());
        for (Role role1:user.getRoles()){
            logger.debug("role:{}", role1.getName());
            for (Permission permission:role1.getPermissions()){
                logger.debug("permission:{}", permission.getName());
            }
        }
    }
}
