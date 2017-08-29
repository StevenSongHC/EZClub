package me.steven.ezclub.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import me.steven.ezclub.entity.College;

@Repository
public class CollegeDao extends GenDao<College> {

	public void save(College college) {
		super.save(college);
	}
	
	public College getCollegeById(String id) {
		return (College) super.findOne(Query.query(Criteria.where("_id").is(id)));
	}
	
	public College getCollegeByCnName(String cnName) {
		return (College) super.findOne(Query.query(Criteria.where("cnName").is(cnName)));
	}
	
	public College getCollegeByEnName(String enName) {
		return (College) super.findOne(Query.query(Criteria.where("enName").is(enName)));
	}
	
	public College getCollegeByShortName(String shortName) {
		return (College) super.findOne(Query.query(Criteria.where("shortName").is(shortName)));
	}
	
	public List<College> getAllCollegeList() {
		return (List<College>) super.findList(new Query().with(new Sort(Direction.ASC, "_id")));
	}
	
	public List<College> getCollegeListByStatus(int status) {
		return (List<College>) super.findList(Query.query(Criteria.where("status").is(status)).with(new Sort(Direction.DESC, "_id")));
	}
	
	public List<College> getAvailableCollegeListByCityId(String cityId) {
		return (List<College>) super.findList(Query.query(
													Criteria.where("city.$id").is(new ObjectId(cityId))
													.norOperator(Criteria.where("status").is(-1))));
	}
	
	public void update(College college) {
		Update update = new Update();
		update.set("cnName", college.getCnName());
		update.set("enName", college.getEnName());
		update.set("shortName", college.getShortName());
		update.set("intro", college.getIntro());
		update.set("badge", college.getBadge());
		update.set("photo", college.getPhoto());
		update.set("status", college.getStatus());
		update.set("city", college.getCity());
		super.update(Query.query(Criteria.where("_id").is(college.getId())), update);
	}
	
	@Override
	protected Class<College> getEntityClass() {
		return College.class;
	}

}
