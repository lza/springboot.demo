package org.leon.springboot.demo.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.leon.springboot.demo.db.model.Person;

@Mapper
public interface PersonMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Person record);

    int insertSelective(Person record);

    Person selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Person record);

    int updateByPrimaryKey(Person record);
}