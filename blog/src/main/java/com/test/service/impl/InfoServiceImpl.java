package com.test.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.Dao.CommentMapper;
import com.test.Dao.InfoMapper;
import com.test.bean.Info;
import com.test.service.IInfoService;

@Transactional
@Service("infoService")
public class InfoServiceImpl implements IInfoService {

	@Resource
	private InfoMapper infoMapper;
	@Override
	public void addClickTimes() {
		// TODO Auto-generated method stub
		Info info=infoMapper.selectByPrimaryKey(1);
		info.setHistoryClickTimes(info.getHistoryClickTimes()+1);
		info.setTodayClickTimes(info.getTodayClickTimes()+1);
		infoMapper.updateByPrimaryKey(info);
	}
	@Override
	public Info getInfoById(Integer id) {
		// TODO Auto-generated method stub
		return infoMapper.selectByPrimaryKey(id);
	}
	@Override
	public void resetTodayClickTimes() {
		// TODO Auto-generated method stub
		Info info=infoMapper.selectByPrimaryKey(1);
		info.setTodayClickTimes(0);
		infoMapper.updateByPrimaryKey(info);
	}

}
