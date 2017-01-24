<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>主管理页-四号基地</title>
    
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
    <script type="text/javascript" charset="utf-8" src="<%=path %>/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=path %>/ueditor/ueditor.all.min.js"> </script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" charset="utf-8" src="<%=path %>/ueditor/lang/zh-cn/zh-cn.js"></script>

	<script type="text/javascript">
		var addFlag=1;
		var editid=5;
		function deleteBlog(id){
			$.ajax({
	        url:'<%=path %>/'+id,
	        type:'delete',
	        data:{},
	       	error: function(request) {
				alert("删除失败");
			},
			success: function(data) {
				alert("删除成功");
				$("#commonLayout_appcreshi").parent().html(data);
				window.location.href="<%=basePath%>manager";
			}
	      });
		}
		function checkFile(id){
				 if($("#file"+id).val()==""){
				 	alert("文件不得为空");
				 	return false;
				 }
				 return true;
		}
		function checkInput(id){
				 if($("input[name='title']").val()==""){
				 	alert("标题不得为空");
				 	return false;
				 }
				 if($("input[name='typename']").val()==""){
				 	alert("类型不得为空");
				 	return false;
				 }
				if (!UE.getEditor('editor').hasContents()){
					alert('请先填写内容!');
					return false;
				}
				$("input[name='content']").val(UE.getEditor('editor').getContent());
				if(addFlag==1){
					$.ajax({
	                cache: true,
	                type: "POST",
	                url:'<%=path %>/post',
	                data:$('#mainblog').serialize(),// 你的formid
	                async: false,
	                error: function(request) {
	                    alert("添加失败");
	                },
	                success: function(data) {
	                	alert("添加成功");
	                    window.location.href="<%=basePath%>manager";
	                	}
	            	});
				}
				else{
				 $.ajax({
	                cache: true,
	                type: "PUT",
	                url:'<%=path %>/'+editid,
	                data:$('#mainblog').serialize(),// 你的formid
	                async: false,
	                error: function(request) {
	                    alert("修改失败");
	                	},
	                success: function(data) {
	                	alert("修改成功");
	                    $("#commonLayout_appcreshi").parent().html(data);
	                     window.location.href="<%=basePath%>manager";
	                	}
		            });
				}
				 return true;
		}
		function beforeAddBlog(){
			addFlag=1;
			$('#myModalLabel').text("新增文章");
			$("input[name='title']").val("");
			$("input[name='typename']").val("");
			$("input[name='content']").val("");
			UE.getEditor('editor').setContent("");
		}
		function beforeEditBlog(id){
			addFlag=0;
			editid=id;
			$('#myModalLabel').text("修改文章");
			var blogdata;
			
			$.ajax({
	        	url:'<%=path %>/blogJson/'+id,
	        	type:'GET',
	        	data:{},
	       		error: function(request) {
					alert("获取内容失败");
				},
				success: function(data) {
					blogdata=data.list;
					$("input[name='title']").val(blogdata.title);
					$("input[name='typename']").val(blogdata.type.typename);
					$("input[name='content']").val("");
					UE.getEditor('editor').setContent(blogdata.content);
					
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
	    <button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal" onclick="beforeAddBlog()">新增文章</button>
	    <table class="table">
			<thead>
			    <tr>
			      <th>序号</th>
			      <th>标题</th>
			      <th>封面</th>		      
			      <th>点击量</th>
			      <th>赞同数</th>
			      <th>评论数</th>				      
			      <th>创建时间</th>
			      <th>操作</th>
			    </tr>
		  	</thead>
		  	<tbody>
			    <c:forEach items="${blogs}" var="s">
			    	<tr>
			    		<td>${s.id}</td>
			    		<td><a href="<%=path %>/${s.id}">${s.title}</a></td>
						<td class="col-md-2"><img class="img-responsive img-thumbnail" style="height: 140px;width:140px"  src="${s.picUrl}"/></td>
						<td>${s.clickTimes}</td>
						<td>${s.agreeWithTimes}</td>
						<td>${s.commentTimes}</td>
						<td><fmt:formatDate value="${s.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒"/></td>
						<td>
							<button class="btn btn-default" data-toggle="modal" data-target="#myModal" onclick="beforeEditBlog(${s.id})">修改</button>

							<input type="button" class="btn btn-default" value="删除" onclick="deleteBlog(${s.id})">
							<a href="<%=path %>/manager/manageComment/${s.id}" class="btn btn-default">管理评论</a>
							<br>
							<br>
							<form action="<%=path %>/imageupload/${s.id}" method="post" enctype="multipart/form-data"  onsubmit="return checkFile(${s.id})">
						 		选择文件:<input  type="file" name="file" id="file${s.id}">
							 	<input type="submit" class="btn btn-default" value="提交封面">
							</form>
						</td>
					
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<!-- 模态框（Modal） -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		    <div class="modal-dialog">
		        <div class="modal-content">
		            <div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title" id="myModalLabel">新增文章</h4>
		            </div>
		            <form  class="form-inline" id="mainblog" name="mainblog">
			            <div class="modal-body">
				            <div class="form-group">
								<label for="name">标题</label>
								<input type="text" style="display:inline;" class="form-control" name="title" id="title" placeholder="请输入标题">
							</div>
							<br><br>
							<div class="form-group">
								<label for="typename">类别</label>
								<input type="text" style="display:inline;" class="form-control" name="typename" id="typename" placeholder="请输入类别">
							</div>
							<br><br>
							<input name="content" type="hidden" id="content">
							<script id="editor" type="text/plain" style="width:100%;height:200px;">${blog.content}</script>
							<script type="text/javascript">
							    //实例化编辑器
							    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
							    var ue = UE.getEditor('editor');   
							</script>
						</div>
			            <div class="modal-footer">
			                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			                <button type="button" class="btn btn-primary" onclick="checkInput()">提交更改</button>
			            </div>
		            </form>
		        </div><!-- /.modal-content -->
		    </div><!-- /.modal -->
		</div>
	</div>
  </body>
</html>
