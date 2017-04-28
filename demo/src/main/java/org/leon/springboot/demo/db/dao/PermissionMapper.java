package org.leon.springboot.demo.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.leon.springboot.demo.db.model.Permission;

import java.util.List;

@Mapper
public interface PermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    int deleteAll();

    List<Permission> getAll();

    Permission findByName(@Param("name")String name);
}