package me.steven.ezclub.dao;

import java.util.List;

import me.steven.ezclub.entity.Content;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class ContentDao extends GenDao<Content> {

	public void save(Content content) {
		super.save(content);
	}
	
	public void delete(Content content) {
		super.delete(content);
	}
	
	public void update(Content content) {
		Update update = new Update();
		update.set("content", content.getContent());
		update.set("order", content.getOrder());
		super.update(Query.query(Criteria.where("_id").is(content.getId())), update);
	}
	
	public Content getContentById(String id) {
		return (Content) super.findOne(Query.query(Criteria.where("_id").is(id)));
	}
	
	public List<Content> getContentListByActivityId(String activityId) {
		return (List<Content>) super.findList(Query.query(Criteria.where("activity.$id").is(new ObjectId(activityId))));
	}
	
	public List<Content> getContentListByClubMemberIdAndActivityId(String clubMemberId, String activityId) {
		return (List<Content>) super.findList(Query.query(Criteria.where("creator.$id").is(new ObjectId(clubMemberId))
															.andOperator(Criteria.where("activity.$id").is(new ObjectId(activityId)))));
	}
	
	public List<Content> getContentListByActivityIdAndType(String activityId, int type) {
		return (List<Content>) super.findList(Query.query(Criteria.where("activity.$id").is(new ObjectId(activityId))
											.andOperator(Criteria.where("type").is(type))));
	}
	
	@Override
	protected Class<Content> getEntityClass() {
		return Content.class;
	}
	
}
