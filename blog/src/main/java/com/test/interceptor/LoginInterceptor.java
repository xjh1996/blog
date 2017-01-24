package com.test.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor implements HandlerInterceptor {
	

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception arg3)
			throws Exception {


	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		//System.out.println("已拦截");
		String url=request.getRequestURI();
		//如果为登录请求
		//不拦截
		if(url.indexOf("login")>0){
			return true;
		}
		
		HttpSession session=request.getSession(); 
		
		String username=(String)session.getAttribute("username");
		
		if(username!=null){
			return true;
		}
		
		request.getRequestDispatcher("/manager/login").forward(request, response);
		
		//不是登录
		return false;
	}

}
