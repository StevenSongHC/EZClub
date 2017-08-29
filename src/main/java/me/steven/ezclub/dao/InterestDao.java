package me.steven.ezclub.dao;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import me.steven.ezclub.entity.Interest;

@Repository
public class InterestDao extends GenDao<Interest> {

	public void save(Interest interest) {
		super.save(interest);
	}
	
	public Interest getInterestById(String id) {
		return (Interest) super.findOne(Query.query(Criteria.where("_id").is(id)));
	}
	
	public Interest getInterestByName(String name) {
		return (Interest) super.findOne(Query.query(Criteria.where("name").is(name)));
	}
	
	@Override
	protected Class<Interest> getEntityClass() {
		return Interest.class;
	}

}
