<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>评论管理-四号基地</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<!-- 新 Bootstrap 核心 CSS 文件 -->
	<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">
	
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
	
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		function deleteBlog(id){
			$.ajax({
	        url:'<%=path %>/comment/'+id,
	        type:'delete',
	        data:{},
	       	error: function(request) {
				alert("删除失败");
			},
			success: function(data) {
				alert("删除成功");
				$("#commonLayout_appcreshi").parent().html(data);
				window.location.href="<%=basePath%>manager/manageComment/${blogId}";
			}
	      });
		}
		</script>
  </head>
  
  <body>
	<div class="container">
	  	<div class="row">
		 	<div class="left col-md-8">
		 		${username},欢迎进入管理页面. 
		 		
		 	</div>
		 	<div class="right">
		 		<a href="<%=path %>/manager/logout" class="btn btn-default">退出</a>
		 	</div>
	    </div>
	    <a href="<%=path %>/manager" class="btn btn-default">回到管理界面</a>
	    <table class="table">
			<thead>
			    <tr>
			      <th>序号</th>
			      <th>用户名</th>
			      <th>邮箱</th>		      
			      <th>创建时间</th>
			      <th>内容</th>
			      <th>操作</th>
			    </tr>
		  	</thead>
		  	<tbody>
			    <c:forEach items="${comments}" var="s">
			    	<tr>
			    		<td>${s.id}</td>
						<td>${s.username}</td>
						<td>${s.email}</td>
						<td>${s.createTime}</td>
						<td>${s.content}</td>
						<td>
							<input class="btn btn-default" type="button" value="删除" onclick="deleteBlog(${s.id})">
						</td>
					
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
  </body>
</html>
