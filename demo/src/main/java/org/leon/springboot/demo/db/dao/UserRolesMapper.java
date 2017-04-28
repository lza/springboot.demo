package org.leon.springboot.demo.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.leon.springboot.demo.db.model.Role;
import org.leon.springboot.demo.db.model.UserRoles;

import java.util.List;

@Mapper
public interface UserRolesMapper {
    int insert(UserRoles record);

    int insertSelective(UserRoles record);

    List<Role> getRolesByUserId(Long userId);

    int deleteByUserId(Long userId);
}