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
    <title>首页-四号基地</title>

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
  <!-- 顶栏部分 -->
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
	        <li class="active"><a href="<%=path %>">首页</a></li>
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
  		<div class="col-md-8">
		     <c:forEach items="${blogs}" var="s">
			  		<a href="<%=path %>/${s.id}"><h1 class="page-header">${s.title}</h1></a>
			  		<div class="row" style="height: 150px;">
			  			<div class="col-md-4 col-lg-4 col-sm-6 col-xs-6 left" >
			  				<img class="img-responsive img-thumbnail" style="height: 140px;width:140px" src="${s.picUrl}"/>
			  			</div>
			  			<div class="right" style="word-break:break-all; word-wrap:break-word">
			  				${s.content}
			  			</div>
			  			
			  		</div>
			  		<br>
			  		<div class="row">
			  			<div class="col-md-3 col-lg-3 col-sm-3 col-xs-3 left">
			                <label style="font-size: 12px;"><fmt:formatDate value="${s.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></label>
			             </div>
			             <div class="col-md-3 col-lg-3 col-sm-3 col-xs-3 left">
			                	<span class="glyphicon glyphicon-eye-open" style="color: gray" aria-hidden="true"/> 
			                	<a style="text-decoration:none;" href="<%=path %>/${s.id }">阅读</a>
			                	(${s.clickTimes})
			             </div>
			             <div class="col-md-3 col-lg-3 col-sm-3 col-xs-3 left">
								<span class="glyphicon glyphicon-edit" style="color: gray" aria-hidden="true"/>  
			                	<a style="text-decoration:none;" href="<%=path %>/${s.id}#commentform">评论</a>
			                	(${s.commentTimes})
			             </div>
			             <div class="col-md-3 col-lg-3 col-sm-3 col-xs-3 left">
			             	<button class="btn" onclick="addagree(${s.id})">  
			             		<span class="glyphicon glyphicon-thumbs-up button" style="color: gray" aria-hidden="true"/>
			             	</button>
			             	<label style="text-align: left" id="agree${s.id}">
			             		推荐(${s.agreeWithTimes })
			             	</label>
			             </div>
					  </div>
		     </c:forEach>

		     <ul class="pagination">
		     	<li><a href="/blog/page/1">&laquo;</a></li>
			     	<c:forEach begin="${pagenum-3<1?1:pagenum-3}" end="${allpages>pagenum+3?pagenum+3:allpages}" var="s">
			     		<c:if test="${pagenum == s}">
						   	<li class="active"><a href="#">${pagenum}</a></li>
						</c:if>
			     		<c:if test="${pagenum != s}">
						   	<li><a href="/blog/page/${s}">${s}</a></li>
						</c:if>
			     		
			     	</c:forEach>
			    <li><a href="/blog/page/${allpages}">&raquo;</a></li>
			</ul>
  		</div>
  		<div class="col-md-4">
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