package com.test.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.Dao.TypeMapper;
import com.test.bean.Type;
import com.test.service.ITypeService;

@Transactional
@Service("typeService")
public class TypeServiceImpl implements ITypeService {
	
	@Resource
	private TypeMapper typeMapper;

	public List<Type> getTypes() {
		// TODO Auto-generated method stub
		return typeMapper.getTypeList();
	}

	public TypeMapper getTypeMapper() {
		return typeMapper;
	}

	public void setTypeMapper(TypeMapper typeMapper) {
		this.typeMapper = typeMapper;
	}

	public boolean isTypeExist(String typename) {
		// TODO Auto-generated method stub
		if(typeMapper.findTypeByName(typename)!=null){
			return true;
		}
		return false;
	}

	public boolean addType(Type type) {
		// TODO Auto-generated method stub
		return typeMapper.insert(type)!=0?true:false;
	}

	public Type getTypeByName(String typename) {
		// TODO Auto-generated method stub
		return typeMapper.findTypeByName(typename);
	}

	@Override
	public void deleteTypeById(int id) {
		// TODO Auto-generated method stub
		typeMapper.deleteByPrimaryKey(id);
	}



}
