package me.steven.ezclub.service;

import java.util.List;

import me.steven.ezclub.dao.ClubDao;
import me.steven.ezclub.entity.Club;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClubService {
	
	@Autowired
	private ClubDao cDao;
	
	public void save(Club club) {
		cDao.save(club);
	}
	
	public Club getClubById(String id) {
		return cDao.getClubById(id);
	}
	
	public Club getClubByCnName(String cnName) {
		return cDao.getClubByCnName(cnName);
	}
	
	/*public Club getClubByEnName(String enName) {
		return cDao.getClubByEnName(enName);
	}*/
	
	/*public List<Club> getAllClubList() {
		return cDao.getAllClubList();
	}*/
	
	public List<Club> getUncheckedClubList() {
		return cDao.getClubListByStatus(0);
	}
	
	public List<Club> getCheckedClubList() {
		return cDao.getClubListByStatus(1);
	}
	
	public List<Club> getClubListByCollegeId(String collegeId) {
		return cDao.getClubListByCollegeId(collegeId);
	}
	
	public void update(Club club) {
		cDao.update(club);
	}

}
