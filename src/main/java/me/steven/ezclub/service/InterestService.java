package me.steven.ezclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.steven.ezclub.dao.InterestDao;
import me.steven.ezclub.entity.Interest;

@Service
public class InterestService {

	@Autowired
	private InterestDao intrDao;
	
	public void save(Interest interest) {
		intrDao.save(interest);
	}
	
	public Interest getInterestById(String id) {
		return intrDao.getInterestById(id);
	}
	
	public Interest getInterestByName(String name) {
		return intrDao.getInterestByName(name);
	}
	
}
