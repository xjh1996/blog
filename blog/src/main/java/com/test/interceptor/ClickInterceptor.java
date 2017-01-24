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
		if((new Date()).getTime()-date.getTime()> (24* 3600000)){
			date=new Date();
			infoService.resetTodayClickTimes();
		}
		infoService.addClickTimes();
		System.out.println(date.toString());
		System.out.println(infoService.getInfoById(1).getHistoryClickTimes());
		return true;
	}

}
