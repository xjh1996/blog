<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,inital-scale=1,user-scanable=no">
    <title>${blog.title}-四号基地</title>

	<!-- 新 Bootstrap 核心 CSS 文件 -->
	<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">
	
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
	
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
			function addagree(id){
			$.ajax({
	        	url:'/blog/addagree/'+id,
	        	type:'GET',
	        	data:{},
	       		error: function(request) {
					alert("点赞失败");
				},
				success: function(data) {
					$("#agree"+id).text("推荐("+data.agreetimes+")");
					
				}
	      	});
		}
	function checkInput(id){
				var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
				 if($("input[name='username']").val()==""){
				 	alert("用户名不得为空");
				 	return false;
				 }
				 if(!filter.test($("input[name='email']").val())){
				 	alert("邮箱格式不正确");
				 	return false;
				 }
				if ($("textarea[name='content']").val()==""){
					alert('请先填写内容!');
					return false;
				}
				 $.ajax({
	                cache: true,
	                type: "POST",
	                url:'<%=path %>/comment/',
	                data:$('#commentform').serialize(),// 你的formid
	                async: false,
	                error: function(request) {
	                    alert("添加失败");
	                },
	                success: function(data) {
	                	alert("添加成功");
	                    window.location.href="<%=basePath%>${blog.id}"
	                }
	            });
				 return true;
			}
		</script>
		<style type="text/css">
			img{
				display: inline-block;
				max-width: 100%;
				max-height: 100%;
			}
		</style>
	</head>
 
  <body>
   <nav class="navbar navbar-default" role="navigation">
	  <div class="container-fluid">
	    <!-- Brand and toggle get grouped for better mobile display -->
	    <div class="navbar-header">
	      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
	        <span class="sr-only">Toggle navigation</span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	      </button>
	      <a class="navbar-brand" href="<%=path %>">四号基地</a>
	    </div>
	
	    <!-- Collect the nav links, forms, and other content for toggling -->
	    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
	      <ul class="nav navbar-nav">
	        <li ><a href="<%=path %>">首页</a></li>
	        <li class="dropdown">
	          <a href="#" class="dropdown-toggle" data-toggle="dropdown">分类<span class="caret"></span></a>
	          <ul class="dropdown-menu" role="menu">
	            <c:forEach items="${types}" var="type">
			     	<li><a href="<%=path %>/searchByType/${type.id}">${type.typename}</a></li>
			    </c:forEach>
	          </ul>
	        </li>
	      </ul>
	      <form action="<%=path %>/searchByWord" method="post" class="navbar-form navbar-right" role="search">
	        <div class="form-group">
	          <input type="text" name="word" class="form-control" placeholder="按文章标题或内容搜索">
	        </div>
	        <button type="submit" class="btn btn-default">搜索</button>
	      </form>
	    </div><!-- /.navbar-collapse -->
	  </div><!-- /.container-fluid -->
	</nav>
	<div class="container">
  		<div class="col-md-8" style="word-break:break-all; word-wrap:break-word">
			  		<h1 >${blog.title}</h1>
			  		 <div style="text-align: right;font-size: 16px;padding-top: 10px">
				        <div style="float: left">
							标签：
				           	<a href="/blog/searchByType/${blog.type.id}" class="label label-info" style="font-size: 16px">${blog.type.typename}</a>
				        </div>
				        <label style="font-size: 15px;text-align: left"><span class="glyphicon glyphicon-eye-open" style="color: gray" aria-hidden="true"></span> <a style="text-decoration:none;" href="<%=path %>/${blog.id }">阅读</a>(${blog.clickTimes})</label>
				        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				        <label style="font-size: 15px;text-align: left"><span class="glyphicon glyphicon-edit" style="color: gray" aria-hidden="true"></span>  <a style="text-decoration:none;" href="<%=path %>/${blog.id }#commentform">评论</a>(${blog.commentTimes})</label>
				        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				        <label style="font-size: 15px;text-align: left">
				        	<button class="btn" onclick="addagree(${blog.id})">  
			             		<span class="glyphicon glyphicon-thumbs-up button" style="color: gray" aria-hidden="true"/>
			             	</button>
			             	<label style="text-align: left" id="agree${blog.id}">
			             		推荐(${blog.agreeWithTimes })
			             	</label>
			             </label>
				        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				        <label style="font-size: 15px"><fmt:formatDate value="${blog.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></label>
				    </div>
			  		<hr>
			  		${blog.content}
	  				<h1 class="page-header">评论区</h1>
	  				<c:forEach items="${comments}" var="s" varStatus="st">
				     	<div style="padding-top: 10px;padding-left: 10px;padding-right: 10px">
					        <span style="color:black;width: 40px">${st.count}楼</span>
					        &nbsp;&nbsp;&nbsp;&nbsp;
					        <span class="glyphicon glyphicon-user" style="color: gray ;width:150px" aria-hidden="true">&nbsp;${s.username}</span>
					        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					        <span class="glyphicon glyphicon-envelope" style="color: gray;width:300px" aria-hidden="true">&nbsp;${s.email}</span>
					        <div style="font-size:16px;padding: 20px 50px 10px 45px;">${s.content}</div>
					        <div style="color:gray;float: right">
					            <i>评论于<fmt:formatDate value="${s.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></i>
					        </div>
					        <hr>
					    </div>
     				</c:forEach>
     				
     				<form action="<%=path %>" method="post" id="commentform" name="commentform">
	     				<div class="form-group">
						    <label for="username">用户名</label>
						    <input type="text" class="form-control" name="username" id="username" placeholder="请输入用户名">
					  	</div>
					  	<div class="form-group">
						    <label for="email">邮箱</label>
						    <input type="text" class="form-control" name="email" id="email" placeholder="请输入邮箱">
					  	</div>
					  	<div class="form-group">
						    <label for="content">文本框</label>
							<textarea class="form-control" id="content" name="content" rows="5"></textarea>
						</div> 
						<input name="blogId" type="hidden" id="blogId" value="${blog.id}">   
						<input type="button" onclick="checkInput()" value="提交"> 	
					</form>
  		</div>
  		<div class="col-md-3">
  			<h3 class="page-header">放只仓鼠压压惊</h3>  			
  			<object type="application/x-shockwave-flash" style="outline:none;" data="http://cdn.abowman.com/widgets/hamster/hamster.swf?" width="300" height="225"><param name="movie" value="http://cdn.abowman.com/widgets/hamster/hamster.swf?"></param><param name="AllowScriptAccess" value="always"></param><param name="wmode" value="opaque"></param></object>
  			<div style="padding-top: 30px;">
	            <fieldset>
	                <div><label>当日请求数：${info.todayClickTimes } 次</label></div>
	                <div><label>历史请求数：${info.historyClickTimes } 次</label></div>
	            </fieldset>
        	</div>
  		</div>
		
  </div>

  </body>
</html>
