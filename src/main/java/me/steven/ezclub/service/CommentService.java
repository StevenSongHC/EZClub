package me.steven.ezclub.service;

import java.util.List;

import me.steven.ezclub.dao.CommentDao;
import me.steven.ezclub.entity.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

	@Autowired
	private CommentDao cmtDao;
	
	public void save(Comment comment) {
		cmtDao.save(comment);
	}
	
	public void delete(Comment comment) {
		cmtDao.delete(comment);
	}
	
	public List<Comment> getCommentListByActivityId(String activityId) {
		return cmtDao.getCommentListByActivityId(activityId);
	}
	
}
