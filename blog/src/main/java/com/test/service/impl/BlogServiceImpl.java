package com.test.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import com.test.Dao.BlogMapper;
import com.test.bean.Blog;
import com.test.bean.Type;
import com.test.service.IBlogService;

@Transactional
@Service("blogService")
public class BlogServiceImpl implements IBlogService{
	
	@Resource
	private BlogMapper blogMapper;
	
	private int subLength=60;
	
	public Blog getBlogById(int id) {
		// TODO Auto-generated method stub
		return blogMapper.findBlogById(id);
	}

	public Blog getBlogByIdWithComment(int id) {
		// TODO Auto-generated method stub
		return blogMapper.findBlogByIdWithComment(id);
	}

	public List<Blog> findBlogsByType(Type type) {
		// TODO Auto-generated method stub
		List<Blog> blogs=blogMapper.findBlogByType(type);
		for (int i = 0; i < blogs.size(); i++) {
			
			String content=blogs.get(i).getContent().replaceAll("<.*?>", "");
			
			blogs.get(i).setContent(content.substring(0, content.length()>subLength?subLength:content.length()));
		}
		return blogs;
	}

	public List<Blog> getAllBlogs() {
		// TODO Auto-generated method stub
		List<Blog> blogs=blogMapper.getBlogList();
		for (int i = 0; i < blogs.size(); i++) {
			
			String content=blogs.get(i).getContent().replaceAll("<.*?>", "");
			
			blogs.get(i).setContent(content.substring(0, content.length()>subLength?subLength:content.length()));
		}
		return blogs;
	}

	public boolean deleteBlogById(Integer id) {
		// TODO Auto-generated method stub
		return blogMapper.deleteById(id)!=0?true:false;
	}

	public boolean updateBlog(Blog record) {
		// TODO Auto-generated method stub
		return blogMapper.updateById(record)!=0?true:false;
	}

	public boolean insert(Blog record) {
		// TODO Auto-generated method stub
		return blogMapper.insert(record)!=0?true:false;
	}

	public BlogMapper getBlogMapper() {
		return blogMapper;
	}

	public void setBlogMapper(BlogMapper blogMapper) {
		this.blogMapper = blogMapper;
	}

	public List<Blog> findBlogsByWord(String word) {
		// TODO Auto-generated method stub
		List<Blog> blogs=blogMapper.findBlogByWord(word);
		for (int i = 0; i < blogs.size(); i++) {
			
			String content=blogs.get(i).getContent().replaceAll("<.*?>", "");
			
			blogs.get(i).setContent(content.substring(0, content.length()>subLength?subLength:content.length()));
		}
		return blogs;
	}

}
