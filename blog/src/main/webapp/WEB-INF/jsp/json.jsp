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
    <script  type="text/javascript" language="javascript"  src="<%=path %>/js/GetAndPutBlog.js" charset="utf-8" ></script>

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
        <a class="navbar-brand" href="javascript:void(0);" onclick="getBlogsByPageNum(1)">四号基地</a>
      </div>

      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav">
          <li id="index" class="active"><a href="javascript:void(0);" onclick="getBlogsByPageNum(1)">首页</a></li>
          <li class="dropdown"  id="types">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">分类<span class="caret"></span></a>
            <ul class="dropdown-menu" role="menu">
              <c:forEach items="${types}" var="type">
                <li><a href="javascript:void(0);" onclick="getBlogsByType(${type.id})">${type.typename}</a></li>
              </c:forEach>
            </ul>
          </li>
        </ul>
        <form class="navbar-form navbar-right" role="search" id="searchForm" onsubmit="return getBlogsByWord();">
          <div class="form-group">
            <input type="text" name="word" class="form-control" placeholder="按文章标题或内容搜索">
          </div>
          <input type="button" class="btn btn-default" onclick="getBlogsByWord()" value="搜索">
        </form>
      </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
  </nav>
  <div class="container" >
    <div class="col-md-8" style="word-break:break-all; word-wrap:break-word" id="maindiv">
        <div class="row"></div>
    </div>
    <div class="col-md-4">
      <h3 class="page-header">放只仓鼠压压惊</h3>
      <object type="application/x-shockwave-flash" style="outline:none;" data="http://cdn.abowman.com/widgets/hamster/hamster.swf?" width="300" height="225"><param name="movie" value="http://cdn.abowman.com/widgets/hamster/hamster.swf?"></param><param name="AllowScriptAccess" value="always"></param><param name="wmode" value="opaque"></param></object>
      <div style="padding-top: 30px;">
        <fieldset>
          <div><label>当日访问ip数：${info.todayClickTimes } 次</label></div>
          <div><label>历史访问ip数：${info.historyClickTimes } 次</label></div>
        </fieldset>
      </div>
    </div>

  </div>
  <script type="text/javascript">
      function addagree(id){
          $.ajax({
              url:'<%=path %>/addagree/'+id,
              type:'GET',
              data:{},
              error: function(request) {
                  alert("点赞失败");
              },
              success: function(data) {
                  $("#agree"+id).text("推荐("+data.agreetimes+")　");
              }
          });
      }
      function getBlogsByPageNum(pageNum){
          $('#index').addClass('active');
          $('#types').removeClass('active');
          $.ajax({
              url: '<%=path %>/json/page/'+pageNum,
              success: function(data){
                  var parentdiv=$("#maindiv");
                  var blogs=data.blogs;
                  var pageCount=data.allpages;
                  putBlogs(parentdiv,blogs);
                  putPageSelector(parentdiv,pageCount,pageNum);
              },
              error:function(request){
                  alert("获取失败，请刷新页面或检查网络");
              }

          });
      }
      function getBlogById(id){
          $('#index').removeClass('active');
          $('#types').removeClass('active');
          $.ajax({
              url: '<%=path %>/json/blog/'+id,
              success: function(data){
                  var parentdiv=$("#maindiv");
                  var blog=data.blog;
                  var comments=data.comments;
                  putOneBlog(parentdiv,blog,comments);
                  //putBlogs(parentdiv,blogs);
                  //putPageSelector(parentdiv,pageCount,pageNum);
              },
              error:function(request){
                  alert("获取失败，请刷新页面或检查网络");
              }

          });
      }
      function getBlogByIdAndGoToComments(id){
          $('#index').removeClass('active');
          $('#types').removeClass('active');
          $.ajax({
              url: '<%=path %>/json/blog/'+id,
              success: function(data){
                  var parentdiv=$("#maindiv");
                  var blog=data.blog;
                  var comments=data.comments;
                  putOneBlog(parentdiv,blog,comments);
                  gotoComments();
                  //putBlogs(parentdiv,blogs);
                  //putPageSelector(parentdiv,pageCount,pageNum);
              },
              error:function(request){
                  alert("获取失败，请刷新页面或检查网络");
              }

          });
      }
      function getBlogsByType(id){
          $('#index').removeClass('active');
          $('#types').addClass('active');
          $.ajax({
              url: '<%=path %>/json/type/'+id,
              success: function(data){
                  var parentdiv=$("#maindiv");
                  var blogs=data.blogs;
                  //var pageCount=data.allpages;
                  putBlogs(parentdiv,blogs);
                  //putPageSelector(parentdiv,pageCount,pageNum);
              },
              error:function(request){
                  alert("获取失败，请刷新页面或检查网络");
              }

          });
      }
      function getBlogsByWord(){
          $('#index').removeClass('active');
          $('#types').removeClass('active');
          $.ajax({
              cache: true,
              type: "POST",
              url:'<%=path %>/json/searchByWord',
              data:$('#searchForm').serialize(),// 你的formid
              async: false,
              error: function(request) {
                  alert("获取失败，请刷新页面或检查网络");
              },
              success: function(data) {
                  var parentdiv=$("#maindiv");
                  var blogs=data.blogs;
                  //var pageCount=data.allpages;
                  putBlogs(parentdiv,blogs);
                  //putPageSelector(parentdiv,pageCount,pageNum);
              }
          });
          return false;
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
                  getBlogById(id);
              }
          });
          return true;
      }
      $(document).ready(getBlogsByPageNum(1));
  </script>
</body>
</html>