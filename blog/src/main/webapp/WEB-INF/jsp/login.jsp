<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,inital-scale=1,user-scanable=no">
    <title>管理员登录-四号基地</title>

	<!-- 新 Bootstrap 核心 CSS 文件 -->
	<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">
	<style>  
	.col-center-block {  
	    float: none;  
	    display: block;  
	    margin-left: auto;  
	    margin-right: auto;  
	}  
	</style>  
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
	
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
</head>
  
  <body>
  	<div class="container">
  		<div class="col-xs-6 col-md-4 col-center-block">
		    <h1  class="page-header text-center" >管理员登录</h1>
		    <form action="<%=path %>/manager/login" method="post">
		    <div class="form-group">
				<label for="name">用户名：</label>
				<input type="text" class="form-control" name="name" id="name" placeholder="请输入用户名">
			</div>
			<div class="form-group">
				<label for="password">密码：</label>
				<input type="password" class="form-control" name="password" id="password" placeholder="请输入密码">
			</div>
		
		    <input type="submit" class="btn btn-lg btn-primary btn-block" value="登录">
		    </form>
		</div> 
    </div>
  </body>
</html>
