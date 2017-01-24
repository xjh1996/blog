package com.test.Dao;

import java.util.List;

import com.test.bean.Blog;
import com.test.bean.Type;

public interface BlogMapper {
    Blog findBlogById(Integer id);
    
    Blog findBlogByIdWithComment(Integer id);
    
    List<Blog> findBlogByType(Type type);
    
    List<Blog> getBlogList();
    
    List<Blog> findBlogByWord(String text);
    
    int deleteById(Integer id);
    
    int updateById(Blog record);
    
    int insert(Blog record);
}