package com.test.service;

import java.util.List;

import com.test.bean.Blog;
import com.test.bean.Type;

public interface IBlogService {
    Blog getBlogById(int id);
    
    Blog getBlogByIdWithComment(int id);
    List<Blog> findBlogsByType(Type type);
    
    List<Blog> getAllBlogs();
    
    List<Blog> findBlogsByWord(String word);
    
    boolean deleteBlogById(Integer id);
    
    boolean updateBlog(Blog record);
    
    boolean insert(Blog record);
	
}
