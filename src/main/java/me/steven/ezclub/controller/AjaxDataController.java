package me.steven.ezclub.controller;

import java.util.HashMap;
import java.util.Map;

import me.steven.ezclub.service.CityService;
import me.steven.ezclub.service.CollegeService;
import me.steven.ezclub.service.ProvinceService;
import me.steven.ezclub.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("ajax")
public class AjaxDataController {

	@Autowired
	private UserService uService;
	@Autowired
	private ProvinceService pvService;
	@Autowired
	private CityService ctService;
	@Autowired
	private CollegeService clgService;
	
	@RequestMapping(value = "is_email_existed", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> isEmailExisted(String email) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (uService.getUserByEmail(email) == null) {
			result.put("code", 0);
		}
		return result;
	}
	
	@RequestMapping(value = "is_nickname_existed", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> isNicknameExisted(String nickname) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (uService.getUserByNickname(nickname) == null) {
			result.put("code", 0);
		}
		return result;
	}
	
	@RequestMapping(value = "load_province_list", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public Map<String, Object> loadProvinceList() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", pvService.getProvinceList());
		return result;
	}
	
	@RequestMapping(value = "load_city_list_by_province", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> loadCityListByProvinceId(@RequestParam(value="province") String pvid) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", ctService.getCityListByProvinceId(pvid));
		return result;
	}
	
	@RequestMapping(value = "load_college_list_by_city", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> loadCollegeListByCityId(@RequestParam(value="city") String ctid) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", clgService.getAvailableCollegeListByCityId(ctid));
		return result;
	}
	
}
