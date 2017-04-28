package org.leon.springboot.demo.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.leon.springboot.demo.db.model.RolePermissions;

@Mapper
public interface RolePermissionsMapper {
    int insert(RolePermissions record);

    int insertSelective(RolePermissions record);

    int deleteByRoleId(Long roleId);
}