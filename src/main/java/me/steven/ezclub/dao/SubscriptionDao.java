package me.steven.ezclub.dao;

import java.util.List;

import me.steven.ezclub.entity.Subscription;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionDao extends GenDao<Subscription> {

	public void save(Subscription subscription) {
		super.save(subscription);
	}
	
	public void delete(Subscription subscription) {
		super.delete(subscription);
	}
	
	public void update(Subscription subscription) {
		Update update = new Update();
		update.set("unreadActivities", subscription.getUnreadActivities());
		super.update(Query.query(Criteria.where("_id").is(subscription.getId())), update);
	}
	
	public Subscription getSubscriptionByUserIdAndClubId(String userId, String clubId) {
		return (Subscription) super.findOne(Query.query(Criteria.where("user.$id").is(new ObjectId(userId))
												.andOperator(Criteria.where("club.$id").is(new ObjectId(clubId)))));
	}
	
	public List<Subscription> getSubscriptionListByUserId(String userId) {
		return (List<Subscription>) super.findList(Query.query(Criteria.where("user.$id").is(new ObjectId(userId))).with(new Sort(Direction.DESC, "subscribeDate")));
	}

	public List<Subscription> getSubscriptionListByClubId(String clubId) {
		return (List<Subscription>) super.findList(Query.query(Criteria.where("club.$id").is(new ObjectId(clubId))).with(new Sort(Direction.DESC, "subscribeDate")));
	}
	
	@Override
	protected Class<Subscription> getEntityClass() {
		return Subscription.class;
	}

}
