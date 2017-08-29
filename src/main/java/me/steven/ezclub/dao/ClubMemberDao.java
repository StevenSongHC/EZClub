package me.steven.ezclub.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import me.steven.ezclub.entity.ClubMember;

@Repository
public class ClubMemberDao extends GenDao<ClubMember> {

	public void save(ClubMember clubMember) {
		super.save(clubMember);
	}
	
	public void delete(ClubMember clubMember) {
		super.delete(clubMember);
	}

	public ClubMember getClubMemberById(String id) {
		return (ClubMember) super.findOne(Query.query(Criteria.where("_id").is(id)));
	}
	
	public ClubMember getClubMemberByUserIdAndClubId(String userId, String clubId) {
		return (ClubMember) super.findOne(Query.query(Criteria.where("user.$id").is(new ObjectId(userId))
													.andOperator(Criteria.where("club.$id").is(new ObjectId(clubId)))));
	}
	
	public List<ClubMember> getClubMemberListByClubId(String clubId) {
		return (List<ClubMember>) super.findList(Query.query(Criteria.where("club.$id").is(new ObjectId(clubId))));
	}

	public List<ClubMember> getClubMemberListByUserId(String userId) {
		return (List<ClubMember>) super.findList(Query.query(Criteria.where("user.$id").is(new ObjectId(userId))));
	}
	
	public List<ClubMember> getClubMemberListByGroupId(String groupId) {
		return (List<ClubMember>) super.findList(Query.query(Criteria.where("group.$id").is(new ObjectId(groupId))));
	}
	
	public List<ClubMember> getClubMemberListByGroupIdAndDepartmentId(String groupId, String departmentId) {
		return (List<ClubMember>) super.findList(Query.query(Criteria.where("group.$id").is(new ObjectId(groupId))
													.andOperator(Criteria.where("department.$id").is(new ObjectId(departmentId)))));
	}
	
	public void update(ClubMember clubMember) {
		Update update = new Update();
		update.set("name", clubMember.getName());
		update.set("contact", clubMember.getContact());
		update.set("department", clubMember.getDepartment());
		update.set("group", clubMember.getGroup());
		super.update(Query.query(Criteria.where("_id").is(clubMember.getId())), update);
	}
	
	@Override
	protected Class<ClubMember> getEntityClass() {
		return ClubMember.class;
	}

}
