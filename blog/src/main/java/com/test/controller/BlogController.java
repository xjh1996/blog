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
	@Resource
	private IBlogService blogService;
	
	@Resource
	private ITypeService typeService;
	
	@Resource
	private ICommentService commentService;
	
	@Resource
	private IInfoService infoService;
	
	private int pageCount=6;
	
	
	
	@RequestMapping("index")
	public String toIndex(Model model){
		int pageNum=1;
		List<Blog> blogs=this.blogService.getAllBlogs();
		int allpages=(blogs.size()/pageCount)+(blogs.size()%pageCount==0?0:1);
		
		Collections.reverse(blogs);
		blogs=blogs.subList((pageNum-1)*pageCount, pageNum*pageCount>blogs.size()?blogs.size():pageNum*pageCount);
		model.addAttribute("blogs",blogs);
		
		List<Type> types=this.typeService.getTypes();
		
		Info info=this.infoService.getInfoById(1);
		model.addAttribute("info", info);
		
		model.addAttribute("types",types);
		model.addAttribute("pagenum",pageNum);
		model.addAttribute("allpages",allpages);
		return "index";
		
	}
	
	@RequestMapping("searchByType/{id}")
	public String toSearchByType(Model model,@PathVariable("id") Integer id){
		List<Type> types=this.typeService.getTypes();
		model.addAttribute("types",types);
		Type type=new Type();
		type.setId(id);
		List<Blog> blogs=this.blogService.findBlogsByType(type);
		model.addAttribute("blogs",blogs);		
		Info info=this.infoService.getInfoById(1);
		model.addAttribute("info", info);
		return "searchByType";		
	}
	
	@RequestMapping("searchByWord")
	public String toSearchByWord(Model model,HttpServletRequest request) throws UnsupportedEncodingException{
		String word=new String(request.getParameter("word").getBytes("ISO-8859-1"),"UTF-8");
		
		List<Blog> blogs=this.blogService.findBlogsByWord(word);
		List<Type> types=this.typeService.getTypes();
		model.addAttribute("blogs",blogs);	
		model.addAttribute("types",types);
		Info info=this.infoService.getInfoById(1);
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
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public String toOneBlog(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
		List<Type> types=this.typeService.getTypes();
		model.addAttribute("types",types);
		Blog blog=this.blogService.getBlogById(id);
		model.addAttribute("blog",blog);
		blog.setClickTimes(blog.getClickTimes()+1);
		
		List<Comment> comments=commentService.selectByBlogId(blog.getId());
		model.addAttribute("comments",comments);
		blog.setCommentTimes(comments.size());
		blogService.updateBlog(blog);
		Info info=this.infoService.getInfoById(1);
		model.addAttribute("info", info);
		return "showBlog";
		
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public String deleteOneBlog(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
		HttpSession session=request.getSession(); 
		
		String username=(String)session.getAttribute("username");

		if(username!=null){
			blogService.deleteBlogById(id);
			List<Type> types=typeService.getTypes();
			for (int i = 0; i < types.size(); i++) {
				if(blogService.findBlogsByType(types.get(i))==null||blogService.findBlogsByType(types.get(i)).size()<1){
					typeService.deleteTypeById(types.get(i).getId());
				}
				
			}
		}
		return "redirect:/manager";	
		
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public String updateOneBlog(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
		Blog blog=blogService.getBlogById(id);
		
		HttpSession session=request.getSession(); 
		
		String username=(String)session.getAttribute("username");
		if(username!=null){
			String title=request.getParameter("title");
			String content=request.getParameter("content");
			String typename=request.getParameter("typename");
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
			List<Type> types=typeService.getTypes();
			for (int i = 0; i < types.size(); i++) {
				if(blogService.findBlogsByType(types.get(i))==null||blogService.findBlogsByType(types.get(i)).size()<1){
					typeService.deleteTypeById(types.get(i).getId());
				}
				
			}
		}				
		return "redirect:/manager";	
	}
	
	
	@RequestMapping(value="/page/{pageNum}",method=RequestMethod.GET)
	public String getBlogByPage(HttpServletRequest request,Model model,@PathVariable("pageNum") Integer pageNum){
		List<Blog> blogs=this.blogService.getAllBlogs();
		List<Type> types=this.typeService.getTypes();
		int allpages=(blogs.size()/pageCount)+(blogs.size()%pageCount==0?0:1);
		Collections.reverse(blogs);
		blogs=blogs.subList((pageNum-1)*pageCount, pageNum*pageCount>blogs.size()?blogs.size():pageNum*pageCount);

		model.addAttribute("blogs",blogs);
		model.addAttribute("types",types);
		model.addAttribute("pagenum",pageNum);
		model.addAttribute("allpages",allpages);
		return "index";	
	}
	
	@RequestMapping(value="/post",method=RequestMethod.POST)
	public String addOneBlog(HttpServletRequest request,Model model) throws IOException{
		HttpSession session=request.getSession(); 
		
		String username=(String)session.getAttribute("username");
		
		if(username!=null){
			String title=request.getParameter("title");
			System.out.println(title);
			String typename=request.getParameter("typename");
			String content=request.getParameter("content");
			Type type=typeService.getTypeByName(typename);
			if (type==null) {
				type=new Type();
				type.setTypename(typename);
				typeService.addType(type);
				type=typeService.getTypeByName(typename);
			}
			Blog blog=new Blog();
			blog.setAgreeWithTimes(0);
			blog.setClickTimes(0);
			blog.setCommentTimes(0);
			blog.setContent(content);
			blog.setCreateTime(new Date());
			blog.setTitle(title);
			blog.setType(type);
			
			Boolean flag=blogService.insert(blog);
			
			System.out.println(flag);
			
		}
		return "redirect:/manager";	
	}
	
	@RequestMapping("/xmlOrJson")
	@ResponseBody
	public Map<String, Object> xmlOrJson() {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("list", blogService.getAllBlogs());
	    return map;
	}
	
	@RequestMapping("/blogJson/{id}")
	@ResponseBody
	public Map<String, Object> blogJson(@PathVariable("id") Integer id) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("list", blogService.getBlogById(id));
	    return map;
	}
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
	
	@RequestMapping(value="/imageupload/{id}",method=RequestMethod.POST)
	public String imageupload(MultipartFile file,HttpServletRequest request,Model model,HttpSession session,@PathVariable("id") Integer id) throws IOException{
		
		Blog blog=blogService.getBlogById(id);
		String username=(String)session.getAttribute("username");

		if(username!=null){
			if (file!=null) {
				String fileName=file.getOriginalFilename();
				String type=fileName.indexOf(".")!=-1?fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()):null;
				if(type!=null){
					if("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())){
						 String savepath=session.getServletContext().getRealPath("/")+"img"+File.separator;
						 File tempFile = new File(savepath, new Date().getTime() + "" + (int)(Math.random()*1000)+"."+type);
						 if (!tempFile.getParentFile().exists()) {  
					            tempFile.getParentFile().mkdir();  
					      }  
					      if (!tempFile.exists()) {  
					            tempFile.createNewFile();  
					      }  
					      file.transferTo(tempFile);
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
