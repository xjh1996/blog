package com.test.interceptor;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.test.service.IBlogService;
import com.test.service.IInfoService;

public class ClickInterceptor implements HandlerInterceptor {

	public static Set<String> ipSet = new HashSet<String>();
	//��¼����������
	public static Calendar calendar = Calendar.getInstance();
	@Resource
	private IInfoService infoService;

	static {
		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

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
		//���������һ�죬����ս��շ����������������ڼ�¼
        if ((Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis()) > (86400000)) {
            calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			infoService.resetTodayClickTimes();
			ipSet.clear();
		}
        //infoService.resetTodayClickTimes();
        String userAddr = arg0.getRemoteAddr();
		if (!ipSet.contains(userAddr)) {
			infoService.addClickTimes();
			ipSet.add(userAddr);

		}


		return true;
	}

}
