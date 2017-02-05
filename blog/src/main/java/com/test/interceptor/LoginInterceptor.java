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
		//从session取出用户信息
		HttpSession session=request.getSession();
		String username=(String)session.getAttribute("username");
		//若用户信息不为null，则不拦截
		if(username!=null){
			return true;
		}
		//若用户信息为null则转发给登录
		request.getRequestDispatcher("/manager/login").forward(request, response);
		System.out.println("拦截");
		//拦截
		return false;
	}

}
