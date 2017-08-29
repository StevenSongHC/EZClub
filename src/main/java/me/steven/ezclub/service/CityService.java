package me.steven.ezclub.service;

import java.util.List;

import me.steven.ezclub.dao.CityDao;
import me.steven.ezclub.entity.City;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService {

	@Autowired
	private CityDao ctDao;
	
	public void save(City city) {
		ctDao.save(city);
	}
	
	public City getCityById(String id) {
		return ctDao.getCityById(id);
	}
	
	public City getCityByCnName(String cnName) {
		return ctDao.getCityByCnName(cnName);
	}
	
	public List<City> getCityList() {
		return ctDao.getCityList();
	}
	
	public List<City> getCityListByProvinceId(String provinceId) {
		return ctDao.getCityListByProvinceId(provinceId);
	}
	
}
