package me.steven.ezclub.service;

import java.util.List;

import me.steven.ezclub.dao.ProvinceDao;
import me.steven.ezclub.entity.Province;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProvinceService {
	
	@Autowired
	private ProvinceDao pvDao;
	
	public void save(Province province) {
		pvDao.save(province);
	}
	
	public Province getProvinceById(String id) {
		return pvDao.getProvinceById(id);
	}
	
	public Province getProvinceByCnName(String cnName) {
		return pvDao.getProvinceByCnName(cnName);
	}

	public List<Province> getProvinceList() {
		return pvDao.getProvinceList();
	}
	
}
