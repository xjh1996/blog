package com.test.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.test.bean.Blog;
import com.test.bean.Comment;
import com.test.bean.Type;
import com.test.service.IBlogService;
import com.test.service.ICommentService;
import com.test.service.ITypeService;

@Controller
public class CommentController {
	
	@Resource
	private IBlogService blogService;
	

	
	@Resource
	private ICommentService commentService;
	
	private void refreshCommentTimes(int id) {
		Blog blog=blogService.getBlogById(id);
		List<Comment> comments=commentService.selectByBlogId(id);
		blog.setCommentTimes(comments.size());
		blogService.updateBlog(blog);
	}

	@RequestMapping(value="comment/{id}",method=RequestMethod.DELETE)
	public String deleteOneComment(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
		HttpSession session=request.getSession(); 
		String[] urlString=request.getHeader("Referer").split("/");
		int blogId=Integer.parseInt(urlString[urlString.length-1]);
		
		
		String username=(String)session.getAttribute("username");
		
		if(username!=null){
			commentService.deleteCommentById(id);
			refreshCommentTimes(blogId);
		}
		return "redirect:/manager/manageComment/"+urlString[urlString.length-1];	
		
	}
	
	
	@RequestMapping(value="comment",method=RequestMethod.POST)
	public String addOneComment(HttpServletRequest request,Model model){
		
		String email=request.getParameter("email");
		//System.out.println(Integer.parseInt(request.getParameter("blogId")));
		
		int blogId=Integer.parseInt(request.getParameter("blogId"));
		String content=request.getParameter("content");
		String username=request.getParameter("username");
		
		Comment comment = new Comment();
		
		comment.setAgreeWithTimes(0);
		comment.setBlogId(blogId);
		comment.setContent(content);
		comment.setCreateTime(new Date());
		comment.setEmail(email);
		comment.setUsername(username);
		
		commentService.addComment(comment);
		
		refreshCommentTimes(blogId);
		
		return "redirect:/"+blogId;	
	}
	
	
}
