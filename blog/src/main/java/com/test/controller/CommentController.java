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

    //两个spring标签注入的service
    @Resource
    private IBlogService blogService;

    @Resource
    private ICommentService commentService;

    //blog的评论数和评论表中实际评论数达成一致
    private void refreshCommentTimes(int id) {
        //获得博客文章及其评论
        Blog blog = blogService.getBlogById(id);
        List<Comment> comments = commentService.selectByBlogId(id);

        //更新评论数
        blog.setCommentTimes(comments.size());
        blogService.updateBlog(blog);
    }

    //删评论，resultful风格
    @RequestMapping(value = "comment/{id}", method = RequestMethod.DELETE)
    public String deleteOneComment(HttpServletRequest request, Model model, @PathVariable("id") Integer id) {
        //得到comment相关的blog的id其实好像在comment.blogId里有,没优化好，
        // 不过这样可以避免null的情况
        String[] urlString = request.getHeader("Referer").split("/");
        int blogId = Integer.parseInt(urlString[urlString.length - 1]);

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username != null) {
            commentService.deleteCommentById(id);
            refreshCommentTimes(blogId);
        }
        return "redirect:/manager/manageComment/" + urlString[urlString.length - 1];

    }

    //添加评论，resultful风格
    @RequestMapping(value = "comment", method = RequestMethod.POST)
    public String addOneComment(HttpServletRequest request, Model model) {

        int blogId = Integer.parseInt(request.getParameter("blogId"));
        //获得评论者的相关内容
        String email = request.getParameter("email");
        String content = request.getParameter("content");
        String username = request.getParameter("username");

        Comment comment = new Comment();

        comment.setAgreeWithTimes(0);
        comment.setBlogId(blogId);
        comment.setContent(content);
        comment.setCreateTime(new Date());
        comment.setEmail(email);
        comment.setUsername(username);

        commentService.addComment(comment);
        //更新相关博客的评论数
        refreshCommentTimes(blogId);

        return "redirect:/" + blogId;
    }


}
