package com.test.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.Dao.CommentMapper;
import com.test.bean.Comment;
import com.test.service.ICommentService;

@Transactional
@Service("commentService")
public class CommentServiceImpl implements ICommentService {

	@Resource
	private CommentMapper commentMapper;
	
	public List<Comment> selectByBlogId(Integer blogId) {
		// TODO Auto-generated method stub
		return commentMapper.selectByBlogId(blogId);
	}


	public boolean addComment(Comment comment) {
		// TODO Auto-generated method stub
		return commentMapper.insert(comment)!=0?true:false;
	}


	public boolean deleteCommentById(Integer id) {
		// TODO Auto-generated method stub
		return commentMapper.deleteByPrimaryKey(id)!=0?true:false;
	}

}
