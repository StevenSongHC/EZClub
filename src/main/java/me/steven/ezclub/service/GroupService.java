package me.steven.ezclub.service;

import java.util.List;

import me.steven.ezclub.dao.GroupDao;
import me.steven.ezclub.entity.Group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

	@Autowired
	private GroupDao gpDao;
	
	public void save(Group group) {
		gpDao.save(group);
	}
	
	public void delete(Group group) {
		gpDao.delete(group);
	}
	
	public Group getGroupById(String id) {
		return gpDao.getGroupById(id);
	}
	
	public Group getGroupByClubIdAndYear(String clubId, int year) {
		return gpDao.getGroupByClubIdAndYear(clubId, year);
	}
	
	public Group getClubDefaultGroup(String clubId) {
		return gpDao.getGroupByClubIdAndYear(clubId, 0);
	}
	
	public List<Group> getClubGroupList(String clubId) {
		return gpDao.getGroupListByClubId(clubId);
	}
	
	public void update(Group group) {
		gpDao.update(group);
	}
	
}
