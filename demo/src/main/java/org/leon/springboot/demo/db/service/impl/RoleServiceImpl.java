package org.leon.springboot.demo.db.service.impl;

import org.leon.springboot.demo.db.dao.RoleMapper;
import org.leon.springboot.demo.db.dao.RolePermissionsMapper;
import org.leon.springboot.demo.db.model.Permission;
import org.leon.springboot.demo.db.model.Role;
import org.leon.springboot.demo.db.model.RolePermissions;
import org.leon.springboot.demo.db.service.RoleService;
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
public class RoleServiceImpl implements RoleService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RolePermissionsMapper rolePermissionsMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(Role role) {
        roleMapper.insertSelective(role);
        updateRolePermission(role);
    }

    private void updateRolePermission(Role role){
        List<Permission> permissions = role.getPermissions();
        if(permissions != null){
            rolePermissionsMapper.deleteByRoleId(role.getId());
            for (Permission permission:role.getPermissions()){
                RolePermissions rolePermissions = new RolePermissions();
                rolePermissions.setRoleId(role.getId());
                rolePermissions.setPermissionsId(permission.getId());
                try {
                    rolePermissionsMapper.insert(rolePermissions);
                } catch (DuplicateKeyException e){
                    logger.warn("exception:" + e);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Role role) {
        roleMapper.deleteByPrimaryKey(role.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Role role) {
        roleMapper.updateByPrimaryKeySelective(role);
        updateRolePermission(role);
    }

    @Override
    public List<Role> getRoles() {
        return roleMapper.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll() {
        roleMapper.deleteAll();
    }

    @Override
    public Role findByName(String name) {
        return roleMapper.findByName(name);
    }

    @Override
    public Role findById(Long id) {
        return roleMapper.findById(id);
    }
}
