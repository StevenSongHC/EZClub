package me.steven.ezclub.dao;

import java.util.List;

import me.steven.ezclub.entity.Group;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class GroupDao extends GenDao<Group> {

	public void save(Group group) {
		super.save(group);
	}
	
	public void delete(Group group) {
		super.delete(group);
	}
	
	public void update(Group group) {
		Update update = new Update();
		update.set("year", group.getYear());
		update.set("wholePhoto", group.getWholePhoto());
		update.set("newbiePhoto", group.getNewbiePhoto());
		super.update(Query.query(Criteria.where("_id").is(group.getId())), update);
	}
	
	public Group getGroupById(String id) {
		return (Group) super.findOne(Query.query(Criteria.where("_id").is(id)));
	}
	
	public Group getGroupByClubIdAndYear(String clubId, int year) {
		return (Group) super.findOne(Query.query(Criteria.where("club.$id").is(new ObjectId(clubId))
				.andOperator(Criteria.where("year").is(year))));
	}
	
	public List<Group> getGroupListByClubId(String clubId) {
		return (List<Group>) super.findList(Query.query(Criteria.where("club.$id").is(new ObjectId(clubId)))
											.with(new Sort(Direction.DESC, "year")));
	}
	
	@Override
	protected Class<Group> getEntityClass() {
		return Group.class;
	}

}
