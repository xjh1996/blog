package com.test.interceptor;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.test.service.IBlogService;
import com.test.service.IInfoService;

public class ClickInterceptor implements HandlerInterceptor {
	//记录访问日期用
	public static Date date=new Date();
	@Resource
	private IInfoService infoService;
	

	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}


	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}


	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws Exception {
		// TODO Auto-generated method stub
		//若日期相隔一天，则清空今日访问量，并将新日期记录
		//不太合理，应强制将获取到的当前日期的年月日取出，将时分秒舍去
		if((new Date()).getTime()-date.getTime()> (24* 3600000)){
			date=new Date();
			infoService.resetTodayClickTimes();
		}
		//每次请求都会增加一次访问量
		infoService.addClickTimes();
		return true;
	}

}
