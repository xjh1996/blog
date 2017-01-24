package com.test.service;

import java.util.List;

import com.test.bean.Comment;

public interface ICommentService {
	public List<Comment> selectByBlogId(Integer blogId);
	
	public boolean addComment(Comment comment);
	
	public boolean deleteCommentById(Integer id);
	
}
