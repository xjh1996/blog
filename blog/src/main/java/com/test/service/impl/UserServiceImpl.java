package com.test.service.impl;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.Dao.UserMapper;
import com.test.bean.User;
import com.test.service.IUserService;

@Transactional
@Service("userService1")
public class UserServiceImpl implements IUserService {
	@Resource
	private UserMapper userMapper;
	 
	public User getUserById(int userId) {
		// TODO Auto-generated method stub
		return this.userMapper.selectByPrimaryKey(userId);
	}

}
