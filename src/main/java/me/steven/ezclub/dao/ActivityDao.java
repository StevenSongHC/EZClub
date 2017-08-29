package me.steven.ezclub.dao;

import java.util.List;

import me.steven.ezclub.entity.Activity;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class ActivityDao extends GenDao<Activity> {

	public void save(Activity activity) {
		super.save(activity);
	}
	
	public void delete(Activity activity) {
		super.delete(activity);
	}
	
	public Activity getActivityById(String id) {
		return (Activity) super.findOne(Query.query(Criteria.where("_id").is(id)));
	}
	
	public Activity getActivityByClubIdAndTitle(String clubId, String title) {
		return (Activity) super.findOne(Query.query(Criteria.where("club.$id").is(new ObjectId(clubId))
											.andOperator(Criteria.where("title").is(title))));
	}
	
	public List<Activity> getAllActivityListByClubId(String clubId) {
		return (List<Activity>) super.findList(Query.query(Criteria.where("club.$id").is(new ObjectId(clubId))).with(new Sort(Direction.DESC, "updateDate")));
	}
	
	public List<Activity> getFinishedActivityListByClubId(String clubId) {
		return (List<Activity>) super.findList(Query.query(Criteria.where("club.$id").is(new ObjectId(clubId))
											.andOperator(Criteria.where("isFinished").is(true)))
											.with(new Sort(Direction.DESC, "updateDate")));
	}
	
	public void update(Activity activity) {
		Update update = new Update();
		update.set("title", activity.getTitle());
		update.set("updateDate", activity.getUpdateDate());
		update.set("isFinished", activity.getIsFinished());
		update.set("editors", activity.getEditors());
		super.update(Query.query(Criteria.where("_id").is(activity.getId())), update);
	}
	
	@Override
	protected Class<Activity> getEntityClass() {
		return Activity.class;
	}

}
