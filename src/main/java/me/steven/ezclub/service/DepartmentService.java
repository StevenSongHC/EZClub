package me.steven.ezclub.service;

import java.util.List;

import me.steven.ezclub.dao.DepartmentDao;
import me.steven.ezclub.entity.Department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentDao deptDao;
	
	public void save(Department department) {
		deptDao.save(department);
	}
	
	public Department getDepartmentById(String id) {
		return deptDao.getDepartmentById(id);
	}
	
	public List<Department> getDepartmentListByClubId(String clubId) {
		return deptDao.getDepartmentListByClubId(clubId);
	}
	
}
