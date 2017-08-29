package me.steven.ezclub.service;

import java.util.List;

import me.steven.ezclub.dao.CollegeDao;
import me.steven.ezclub.entity.College;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollegeService {

	@Autowired
	private CollegeDao clgDao;
	
	public void save(College college) {
		clgDao.save(college);
	}
	
	public College getCollegeById(String id) {
		return clgDao.getCollegeById(id);
	}
	
	public College getCollegeByCnName(String cnName) {
		return clgDao.getCollegeByCnName(cnName);
	}
	
	/*public College getCollegeByEnName(String enName) {
		return clgDao.getCollegeByEnName(enName);
	}*/
	
	/*public College getCollegeByShortName(String shortName) {
		return clgDao.getCollegeByShortName(shortName);
	}*/
	
	/*public List<College> getAllCollegeList() {
		return clgDao.getAllCollegeList();
	}*/
	
	public List<College> getUncheckedCollegeList() {
		return clgDao.getCollegeListByStatus(0);
	}
	
	public List<College> getCheckedCollegeList() {
		return clgDao.getCollegeListByStatus(1);
	}
	
	/*public List<College> getHiddedCollegeList() {
		return clgDao.getCollegeListByStatus(-1);
	}*/
	
	/*public List<College> getAvailableCollegeList() {
		List<College> collegeList = new ArrayList<College>();
		collegeList.addAll(getCheckedCollegeList());
		collegeList.addAll(getUncheckedCollegeList());
		return collegeList;
	}*/
	
	// availableList = uncheck + check
	public List<College> getAvailableCollegeListByCityId(String cityId) {
		return clgDao.getAvailableCollegeListByCityId(cityId);
	}
	
	public void update(College college) {
		clgDao.update(college);
	}
	
}
