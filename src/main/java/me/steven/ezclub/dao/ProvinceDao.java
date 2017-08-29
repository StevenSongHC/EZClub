package me.steven.ezclub.dao;

import java.util.List;

import me.steven.ezclub.entity.Province;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ProvinceDao extends GenDao<Province> {

	public void save(Province province) {
		super.save(province);
	}
	
	public Province getProvinceById(String id) {
		return (Province) super.findOne(Query.query(Criteria.where("_id").is(id)));
	}
	
	public Province getProvinceByCnName(String cnName) {
		return (Province) super.findOne(Query.query(Criteria.where("cnName").is(cnName)));
	}
	
	public List<Province> getProvinceList() {
		return (List<Province>) super.findList(new Query().with(new Sort(Direction.ASC, "_id")));
	}
	
	@Override
	protected Class<Province> getEntityClass() {
		return Province.class;
	}

}
