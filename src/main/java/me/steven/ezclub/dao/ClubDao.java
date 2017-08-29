package me.steven.ezclub.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import me.steven.ezclub.entity.Club;

@Repository
public class ClubDao extends GenDao<Club> {

	public void save(Club club) {
		super.save(club);
	}
	
	public Club getClubById(String id) {
		return (Club) super.findOne(Query.query(Criteria.where("_id").is(id)));
	}
	
	public Club getClubByCnName(String cnName) {
		return (Club) super.findOne(Query.query(Criteria.where("cnName").is(cnName)));
	}
	
	public Club getClubByEnName(String enName) {
		return (Club) super.findOne(Query.query(Criteria.where("enName").is(enName)));
	}
	
	public List<Club> getAllClubList() {
		return (List<Club>) super.findList(new Query().with(new Sort(Direction.ASC, "_id")));
	}
	
	public List<Club> getClubListByStatus(int status) {
		return (List<Club>) super.findList(Query.query(Criteria.where("status").is(status)).with(new Sort(Direction.DESC, "_id")));
	}
	
	public List<Club> getClubListByCollegeId(String collegeId) {
		return (List<Club>) super.findList(Query.query(Criteria.where("college.$id").is(new ObjectId(collegeId))));
	}
	
	public void update(Club club) {
		Update update = new Update();
		update.set("cnName", club.getCnName());
		update.set("enName", club.getEnName());
		update.set("intro", club.getIntro());
		update.set("badge", club.getBadge());
		update.set("status", club.getStatus());
		update.set("college", club.getCollege());
		update.set("manager", club.getManager());
		update.set("tags", club.getTags());
		super.update(Query.query(Criteria.where("_id").is(club.getId())), update);
	}
	
	@Override
	protected Class<Club> getEntityClass() {
		return Club.class;
	}

}
