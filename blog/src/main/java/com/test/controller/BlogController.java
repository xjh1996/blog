package com.test.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.test.bean.Blog;
import com.test.bean.Comment;
import com.test.bean.Info;
import com.test.bean.Type;
import com.test.service.IBlogService;
import com.test.service.ICommentService;
import com.test.service.IInfoService;
import com.test.service.ITypeService;

@Controller
public class BlogController {
	//四个spring标签注入的service
	@Resource
	private IBlogService blogService;
	
	@Resource
	private ITypeService typeService;
	
	@Resource
	private ICommentService commentService;
	
	@Resource
	private IInfoService infoService;

	//一个用于管理博客每页条数的变量
	private int pageCount=6;
	
	
	//主页显示
	@RequestMapping("index")
	public String toIndex(Model model){
		//主页取分页的第一页内容
		int pageNum=1;
		List<Blog> blogs=this.blogService.getAllBlogs();
		int allpages=(blogs.size()/pageCount)+(blogs.size()%pageCount==0?0:1);
		//倒序排列原以为可以按照日期从新到老排列，
		// 没想到没加order by不理想
		Collections.reverse(blogs);
		//在所有blog中取出第一页内容，本可以用mysql的sql语句实现分页，不过我目前的实现方式还好，先不优化了
		blogs=blogs.subList((pageNum-1)*pageCount, pageNum*pageCount>blogs.size()?blogs.size():pageNum*pageCount);
		//获取所有文章种类加入供视图渲染使用
		List<Type> types=this.typeService.getTypes();
		//拿到网站相关参数，访问量什么的
		Info info=this.infoService.getInfoById(1);

		//将所有要用到的加入model
		model.addAttribute("info", info);
		model.addAttribute("blogs",blogs);
		model.addAttribute("types",types);
		model.addAttribute("pagenum",pageNum);
		model.addAttribute("allpages",allpages);
		return "index";
		
	}

    @RequestMapping("json")
    public String toJson(Model model) {
        //获取所有文章种类加入供视图渲染使用
        List<Type> types = this.typeService.getTypes();
        //拿到网站相关参数，访问量什么的
        Info info = this.infoService.getInfoById(1);
        //将所有要用到的加入model
        model.addAttribute("info", info);
        model.addAttribute("types", types);
        return "json";

    }

    @RequestMapping("json/page/{pageNum}")
    @ResponseBody
    public Map<String, Object> getBlogsByPage(@PathVariable("pageNum") Integer pageNum) {
        Map<String, Object> map = new HashMap<String, Object>();
        //先拿到所有博客再根据当前页选取
        List<Blog> blogs = this.blogService.getAllBlogs();
        int allpages = (blogs.size() / pageCount) + (blogs.size() % pageCount == 0 ? 0 : 1);
        Collections.reverse(blogs);
        blogs = blogs.subList((pageNum - 1) * pageCount, pageNum * pageCount > blogs.size() ? blogs.size() : pageNum * pageCount);
        //将所有要用到的加入model
        map.put("blogs", blogs);
        map.put("allpages", allpages);
        return map;
    }

    //具体显示某篇博客
    @RequestMapping(value = "/json/blog/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getOneBlog(@PathVariable("id") Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        //获得根据id这篇博客
        Blog blog = this.blogService.getBlogById(id);
        //计算一次点击量
        blog.setClickTimes(blog.getClickTimes() + 1);
        //获得文章评论
        List<Comment> comments = commentService.selectByBlogId(blog.getId());
        //刷新一次博客评论数
        blog.setCommentTimes(comments.size());
        this.blogService.updateBlog(blog);

        map.put("blog", blog);
        map.put("comments", comments);
        return map;
    }

    @RequestMapping("/json/type/{id}")
    @ResponseBody
    public Map<String, Object> getBlogsByType(@PathVariable("id") Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        //根据类型id找博客文章
        Type type = new Type();
        type.setId(id);
        List<Blog> blogs = this.blogService.findBlogsByType(type);

        //将所有要用到的加入model
        map.put("blogs", blogs);
        //map.put("allpages", allpages);
        return map;
    }

    @RequestMapping("/json/searchByWord")
    @ResponseBody
    public Map<String, Object> getBlogsByWord(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<String, Object>();
        String word = request.getParameter("word");
        //System.out.println(request.getParameter("word"));
        //根据类型id找博客文章
        //根据关键词查找文章，标题和内容均可
        List<Blog> blogs = this.blogService.findBlogsByWord(word);

        //将所有要用到的加入model
        map.put("blogs", blogs);
        //map.put("allpages", allpages);
        return map;
    }

	@RequestMapping("searchByType/{id}")
	public String toSearchByType(Model model,@PathVariable("id") Integer id){
		//获取所有文章种类加入供视图渲染使用
		List<Type> types=this.typeService.getTypes();
		//根据类型id找博客文章
		Type type=new Type();
		type.setId(id);
		List<Blog> blogs=this.blogService.findBlogsByType(type);
		//拿到网站相关参数，访问量什么的
		Info info=this.infoService.getInfoById(1);
		//将所有要用到的加入model
		model.addAttribute("info", info);
		model.addAttribute("types",types);
		model.addAttribute("blogs",blogs);
		return "searchByType";		
	}
	
	@RequestMapping("searchByWord")
	public String toSearchByWord(Model model,HttpServletRequest request) throws UnsupportedEncodingException{
		//不知为何查找信息传到服务器时出现中文乱码，没找到原因，就这么解决吧
		String word=new String(request.getParameter("word").getBytes("ISO-8859-1"),"UTF-8");
		//根据关键词查找文章，标题和内容均可
		List<Blog> blogs=this.blogService.findBlogsByWord(word);
		//获取所有文章种类加入供视图渲染使用
		List<Type> types=this.typeService.getTypes();
		//拿到网站相关参数，访问量什么的
		Info info=this.infoService.getInfoById(1);

		//将所有要用到的加入model
		model.addAttribute("blogs",blogs);	
		model.addAttribute("types",types);
		model.addAttribute("info", info);
		return "index";		
	}
	
	/*
	@RequestMapping("showBlog/{id}")
	public String toOneBlog(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
		List<Type> types=this.typeService.getTypes();
		model.addAttribute("types",types);
		Blog blog=this.blogService.getBlogByIdWithComment(id);
		model.addAttribute("blog",blog);
		return "showBlog";
		
	}*/
	//具体显示某篇博客
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public String toOneBlog(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
		//获取所有文章种类加入供视图渲染使用
		List<Type> types=this.typeService.getTypes();
		//获得根据id这篇博客
		Blog blog=this.blogService.getBlogById(id);
		//计算一次点击量
		blog.setClickTimes(blog.getClickTimes() + 1);
		//获得文章评论
		List<Comment> comments=commentService.selectByBlogId(blog.getId());
		//刷新一次博客评论数
		blog.setCommentTimes(comments.size());
		this.blogService.updateBlog(blog);
		//拿到网站相关参数，访问量什么的
		Info info=this.infoService.getInfoById(1);

		//将所有要用到的加入model
		model.addAttribute("types",types);
		model.addAttribute("blog",blog);
		model.addAttribute("comments", comments);
		model.addAttribute("info", info);
		return "showBlog";
		
	}

	//删除某篇博客
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public String deleteOneBlog(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
		//身份验证
		HttpSession session=request.getSession(); 
		String username=(String)session.getAttribute("username");
		if(username!=null){
			//根据ID删除相关博客和评论
			blogService.deleteBlogById(id);
			//删评论这种方式并不好，建议加条根据blogid删评论的sql语句
			List<Type> types=typeService.getTypes();
			for (int i = 0; i < types.size(); i++) {
				if(blogService.findBlogsByType(types.get(i))==null||blogService.findBlogsByType(types.get(i)).size()<1){
					typeService.deleteTypeById(types.get(i).getId());
				}
				
			}
		}
		return "redirect:/manager";	
		
	}
	//修改某篇博客
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public String updateOneBlog(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
		//获得这篇博客
		Blog blog=blogService.getBlogById(id);
		//身份验证
		HttpSession session=request.getSession();
		String username=(String)session.getAttribute("username");
		if(username!=null){
			//拿到最新的文章相关参数并更新文章
			String title=request.getParameter("title");
			String content=request.getParameter("content");
			String typename=request.getParameter("typename");
			//若修改了类型且类型不存在则新建，若存在，则取出
			Type type=typeService.getTypeByName(typename);
			if (type==null) {
				type=new Type();
				type.setTypename(typename);
				typeService.addType(type);
				type=typeService.getTypeByName(typename);
			}
			blog.setTitle(title);
			blog.setContent(content);
			blog.setType(type);
			blogService.updateBlog(blog);
			//删去类型下无博客的类型
			List<Type> types=typeService.getTypes();
			for (int i = 0; i < types.size(); i++) {
				if(blogService.findBlogsByType(types.get(i))==null||blogService.findBlogsByType(types.get(i)).size()<1){
					typeService.deleteTypeById(types.get(i).getId());
				}
				
			}
		}				
		return "redirect:/manager";	
	}
	
	//博客分页服务函数，建议分页写一个单独的函数降低耦合度
	@RequestMapping(value="/page/{pageNum}",method=RequestMethod.GET)
	public String getBlogByPage(HttpServletRequest request,Model model,@PathVariable("pageNum") Integer pageNum){

		//获取所有文章种类加入供视图渲染使用
		List<Type> types=this.typeService.getTypes();
		//先拿到所有博客再根据当前页选取
		List<Blog> blogs=this.blogService.getAllBlogs();
		int allpages=(blogs.size()/pageCount)+(blogs.size()%pageCount==0?0:1);
		Collections.reverse(blogs);
		blogs=blogs.subList((pageNum-1)*pageCount, pageNum*pageCount>blogs.size()?blogs.size():pageNum*pageCount);
		//将所有要用到的加入model
		model.addAttribute("blogs",blogs);
		model.addAttribute("types",types);
		model.addAttribute("pagenum",pageNum);
		model.addAttribute("allpages",allpages);
		return "index";	
	}
	//新增博客
	@RequestMapping(value="/post",method=RequestMethod.POST)
	public String addOneBlog(HttpServletRequest request,Model model) throws IOException{
		//身份验证
		HttpSession session=request.getSession();
		String username=(String)session.getAttribute("username");
		if(username!=null){
			//获取相关参数
			String title=request.getParameter("title");
			//System.out.println(title);
			String typename=request.getParameter("typename");
			String content=request.getParameter("content");
			//若修改了类型且类型不存在则新建，若存在，则取出
			Type type=typeService.getTypeByName(typename);
			if (type==null) {
				type=new Type();
				type.setTypename(typename);
				typeService.addType(type);
				type=typeService.getTypeByName(typename);
			}
			//新增博客实现
			Blog blog=new Blog();
			blog.setAgreeWithTimes(0);
			blog.setClickTimes(0);
			blog.setCommentTimes(0);
			blog.setContent(content);
			blog.setCreateTime(new Date());
			blog.setTitle(title);
			blog.setType(type);
			Boolean flag=blogService.insert(blog);
			//System.out.println(flag);
			
		}
		return "redirect:/manager";	
	}
	//json数据测试用
	@RequestMapping("/xmlOrJson")
	@ResponseBody
	public Map<String, Object> xmlOrJson() {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("list", blogService.getAllBlogs());
	    return map;
	}
	//json数据测试用
	@RequestMapping("/blogJson/{id}")
	@ResponseBody
	public Map<String, Object> blogJson(@PathVariable("id") Integer id) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("list", blogService.getBlogById(id));
	    return map;
	}
	//点赞并返回点赞数（json格式）
	@RequestMapping(value="/addagree/{id}")
	@ResponseBody
	public Map<String, Object> addagree(HttpServletRequest request,@PathVariable("id") Integer id){
		Map<String, Object> map = new HashMap<String, Object>();
		Blog blog=blogService.getBlogById(id);
		blog.setAgreeWithTimes(blog.getAgreeWithTimes()+1);
		blogService.updateBlog(blog);
		map.put("agreetimes", blog.getAgreeWithTimes());
		return map;
	}
	//文章封面上传
	@RequestMapping(value="/imageupload/{id}",method=RequestMethod.POST)
	public String imageupload(MultipartFile file,HttpServletRequest request,Model model,HttpSession session,@PathVariable("id") Integer id) throws IOException{
		//根据id得到这篇博客
		Blog blog=blogService.getBlogById(id);
		//登录验证
		String username=(String)session.getAttribute("username");
        System.out.println(request.getParameter("username"));
        //System.out.println(username!=null);
        if (username != null) {
            //System.out.println(file!=null);
            if (file != null) {
                //得到上传封面文件名
				String fileName=file.getOriginalFilename();
				//得到后缀名
				String type=fileName.indexOf(".")!=-1?fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()):null;
				//若后缀名为gif，png或jpg则认为是图片，可以添加
				if(type!=null){
					if("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())){
						//图片存储路径
						String savepath=session.getServletContext().getRealPath("/")+"img"+File.separator;

                        //图片的名称及存储路径
                        File tempFile = new File(savepath, new Date().getTime() + "" + (int)(Math.random()*1000)+"."+type);
						//若路径不存在，则创建
						if (!tempFile.getParentFile().exists()) {
							tempFile.getParentFile().mkdir();
						}
						//若文件不存在，则创建
						if (!tempFile.exists()) {
					    	tempFile.createNewFile();
					    }
						//将图片内容传入新建的文件中
						file.transferTo(tempFile);
						//将封面图片路径设定到blog属性中
					    blog.setPicUrl("/blog/img/"+tempFile.getName());
					    blogService.updateBlog(blog);
					}
				}
			}
		}
		
		return "redirect:/manager";
		
		
	}

	public IBlogService getBlogService() {
		return blogService;
	}

	public void setBlogService(IBlogService blogService) {
		this.blogService = blogService;
	}

	public ITypeService getTypeService() {
		return typeService;
	}

	public void setTypeService(ITypeService typeService) {
		this.typeService = typeService;
	}
	
}
