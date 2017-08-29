package me.steven.ezclub.dao;

import java.util.List;

import me.steven.ezclub.entity.Comment;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDao extends GenDao<Comment> {

	public void save(Comment comment) {
		super.save(comment);
	}
	
	public void delete(Comment comment) {
		super.delete(comment);
	}
	
	public List<Comment> getCommentListByActivityId(String activityId) {
		return (List<Comment>) super.findList(Query.query(Criteria.where("activity.$id").is(new ObjectId(activityId))).with(new Sort(Direction.DESC, "submitDate")));
	}
	
	@Override
	protected Class<Comment> getEntityClass() {
		return Comment.class;
	}
	
}
