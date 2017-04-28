package org.leon.springboot.demo.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.leon.springboot.demo.db.model.User;

import java.util.List;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User findByEmailAndActive(@Param("email") String email, @Param("active") Boolean active);

    int deleteAll();
    List<User> getAll();
}