package com.test.Dao;

import java.util.List;

import com.test.bean.Type;

public interface TypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Type record);

    int insertSelective(Type record);

    Type selectByPrimaryKey(Integer id);
    
    Type findTypeByName(String typename);
    
    List<Type> getTypeList();

    int updateByPrimaryKeySelective(Type record);

    int updateByPrimaryKey(Type record);
}