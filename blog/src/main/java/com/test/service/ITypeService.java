package com.test.service;

import java.util.List;

import com.test.bean.Type;

public interface ITypeService {
	public List<Type> getTypes();
	
	public Type getTypeByName(String typename);
	
	public boolean isTypeExist(String typename);
	
	public boolean addType(Type type);
	
	public void deleteTypeById(int id);
}
