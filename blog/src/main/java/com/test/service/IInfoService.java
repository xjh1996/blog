package com.test.service;

import com.test.bean.Info;

public interface IInfoService {
	public void addClickTimes();
	public void resetTodayClickTimes();
	public Info getInfoById(Integer id);
}
