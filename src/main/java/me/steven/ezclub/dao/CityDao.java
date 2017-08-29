package me.steven.ezclub.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import me.steven.ezclub.entity.City;

@Repository
public class CityDao extends GenDao<City> {

	public void save(City city) {
		super.save(city);
	}
	
	public City getCityById(String id) {
		return (City) super.findOne(Query.query(Criteria.where("_id").is(id)));
	}
	
	public City getCityByCnName(String cnName) {
		return (City) super.findOne(Query.query(Criteria.where("cnName").is(cnName)));
	}
	
	public List<City> getCityList() {
		return (List<City>) super.findList(new Query().with(new Sort(Direction.ASC, "_id")));
	}
	
	public List<City> getCityListByProvinceId(String provinceId) {
		return (List<City>) super.findList(Query.query(Criteria.where("province.$id").is(new ObjectId(provinceId))));
	}
	
	@Override
	protected Class<City> getEntityClass() {
		return City.class;
	}

}
