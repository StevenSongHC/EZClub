package me.steven.ezclub.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import me.steven.ezclub.entity.Department;

@Repository
public class DepartmentDao extends GenDao<Department> {

	public void save(Department department) {
		super.save(department);
	}
	
	public Department getDepartmentById(String id) {
		return (Department) super.findOne(Query.query(Criteria.where("_id").is(id)));
	}
	
	public List<Department> getDepartmentListByClubId(String clubId) {
		return (List<Department>) super.findList(Query.query(Criteria.where("club.$id").is(new ObjectId(clubId))));
	}
	
	@Override
	protected Class<Department> getEntityClass() {
		return Department.class;
	}

}
