package me.steven.ezclub.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.steven.ezclub.entity.City;
import me.steven.ezclub.entity.Club;
import me.steven.ezclub.entity.ClubMember;
import me.steven.ezclub.entity.College;
import me.steven.ezclub.entity.Group;
import me.steven.ezclub.entity.Interest;
import me.steven.ezclub.entity.Province;
import me.steven.ezclub.entity.User;
import me.steven.ezclub.service.ActivityService;
import me.steven.ezclub.service.CityService;
import me.steven.ezclub.service.ClubMemberService;
import me.steven.ezclub.service.ClubService;
import me.steven.ezclub.service.CollegeService;
import me.steven.ezclub.service.DepartmentService;
import me.steven.ezclub.service.GroupService;
import me.steven.ezclub.service.InterestService;
import me.steven.ezclub.service.ProvinceService;
import me.steven.ezclub.service.SubscriptionService;
import me.steven.ezclub.service.UserService;
import me.steven.ezclub.util.CookieUtil;
import me.steven.ezclub.util.MD5Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("")
@SessionAttributes(value={"USER_SESSION"})
public class IndexController {
	
	public static final String USER_SESSION = "USER_SESSION";
	
	@Autowired
	private UserService uService;
	@Autowired
	private ClubService cService;
	@Autowired
	private ProvinceService pvService;
	@Autowired
	private CityService ctService;
	@Autowired
	private CollegeService clgService;
	@Autowired
	private InterestService intrService;
	@Autowired
	private GroupService gpService;
	@Autowired
	private DepartmentService deptService;
	@Autowired
	private ClubMemberService cmService;
	@Autowired
	private ActivityService actService;
	@Autowired
	private SubscriptionService subService;
	
	@RequestMapping("index")
	public String index(ModelMap model) {
		
		return "index";
	}
	
	@RequestMapping("create_college")
	public String createCollege(ModelMap model) {
		// load province list
		model.put("provinceList", pvService.getProvinceList());
		return "create_college";
	}
	
	@RequestMapping(value = "create_college.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doCreateCollege(@RequestParam(value="province") String province,
											   @RequestParam(value="is-new-province") boolean isNewProvince,
											   @RequestParam(value="city") String city,
											   @RequestParam(value="is-new-city") boolean isNewCity,
											   @RequestParam(value="college-cn-name") String collegeCnName,
											   @RequestParam(value="college-en-name", required=false) String collegeEnName,
											   @RequestParam(value="college-short-name", required=false) String collegeShortName,
											   @RequestParam(value="intro", required=false) String intro,
											   @RequestParam(value="file-type") String fileType,
											   @RequestParam(value="badge", required=false) MultipartFile badgeFile,
											   @RequestParam(value="photo", required=false) MultipartFile photoFile,
											   HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		Province pv = null;
		// if create new province, province=Province.cnName
		if (isNewProvince) {
			Province p = pvService.getProvinceByCnName(province);
			// good to create
			if (p == null) {
				Province newProvince = new Province(province);
				pvService.save(newProvince);
				pv = newProvince;
			}
			// use existed province
			else {
				pv = p;
			}
		}
		// else, province=Province.id
		else {
			Province p =pvService.getProvinceById(province);
			// good provinceId
			if (p != null) {
				pv = p;
			}
			// return error_code: unidentified provinceID
			else {
				result.put("code", -3);
				return result;
			}
		}
		// same for city
		City ct = null;
		if (isNewCity) {
			City c = ctService.getCityByCnName(city);
			if (c == null) {
				City newCity = new City(city, pv);
				ctService.save(newCity);
				ct = newCity;
			}
			else {
				ct = c;
			}
		}
		else {
			City c = ctService.getCityById(city);
			if (c != null) {
				ct = c;
			}
			// error_code: unidentified cityId
			else {
				result.put("code", -2);
				return result;
			}
		}
		
		if (clgService.getCollegeByCnName(collegeCnName) != null) {
			// error_code: conflicted College.cnNname
			result.put("code", -1);
			return result;
		}
		
		// create new college
		College college = new College(collegeCnName, collegeEnName, collegeShortName, intro, ct);
		clgService.save(college);
		
		// set badge and photo
		if (badgeFile != null) {
			String saveName = "images/college-badge/" + college.getId() + fileType;
			String savePath = request.getServletContext().getRealPath("") + "/WEB-INF/classes/" + saveName;
			// save badge file
			try {
				badgeFile.transferTo(new File(savePath));
				college.setBadge(saveName);
				clgService.update(college);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (photoFile != null) {
			String saveName = "images/college-photo/" + college.getId() + fileType;
			String savePath = request.getServletContext().getRealPath("") + "/WEB-INF/classes/" + saveName;
			// save badge file
			try {
				photoFile.transferTo(new File(savePath));
				college.setPhoto(saveName);
				clgService.update(college);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping("create_club")
	public String createClub(ModelMap model) {
		model.put("provinceList", pvService.getProvinceList());
		return "create_club";
	}
	
	@RequestMapping(value = "create_club.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doCreateClub(@RequestParam(value="province") String provinceId,
											@RequestParam(value="city") String cityId,
											@RequestParam(value="college") String collegeId,
											@RequestParam(value="clubCnName") String clubCnName,
											@RequestParam(value="clubEnName", required = false) String clubEnName,
											@RequestParam(value="intro", required=false) String intro,
											HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		User currentUser = (User) session.getAttribute(USER_SESSION);
		
		Province province = pvService.getProvinceById(provinceId);
		if (province == null) {
			result.put("code", -4);		// wrong province
			return result;
		}
		
		City city = ctService.getCityById(cityId);
		if (city == null) {
			result.put("code", -3);		// wrong city
			return result;
		}
		
		College college = clgService.getCollegeById(collegeId);
		if (college == null) {
			result.put("code", -2);		// wrong college
			return result;
		}
		
		if (cService.getClubByCnName(clubCnName) != null) {
			result.put("code", -1);		// conflicted College.cnName
			return result;
		}
		
		// save new club																								////////////////////
		Club club = new Club(clubCnName, clubEnName, intro, college);													////    CLUB	////
		cService.save(club);																							////////////////////
		
		// create default group for the new club																		////////////////////
		Group defaultGroup = new Group(club, 0);																		///     GROUP    ///
		gpService.save(defaultGroup);																					////////////////////
		
		// setup manager for the new club																				////////////////////
		ClubMember manager = new ClubMember(currentUser.getNickname(), currentUser, club, defaultGroup);								////   MANAGER  ////
		cmService.save(manager);																						////////////////////
		club.setManager(manager);
		cService.update(club);
		
		result.put("code", 1);
		
		return result;
	}
	
	@RequestMapping("join")
	public String join(ModelMap model) {
		model.put("provinceList", pvService.getProvinceList());
		return "join";
	}
	
	@RequestMapping(value = "join.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doJoin(ModelMap model,
									  String email,
									  String password,
									  String nickname,
									  int sex,
									  String birth,
									  String provinceId,
									  String cityId,
									  String collegeId,
									  @RequestParam(value="interests[]", required=false) String[] interests,
									  HttpSession session,
									  HttpServletRequest request,
									  HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// save basic
		User newUser = new User();
		newUser.setEmail(email);
		newUser.setPassword(MD5Util.encryptCode(password));
		newUser.setNickname(nickname);
		// save extra info
		newUser.setSex(sex);
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = format.parse(birth);
			java.sql.Date birthDate = new java.sql.Date(date.getTime());
			newUser.setBirthDate(birthDate);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		if (!provinceId.equals("0")) {
			newUser.setProvince(pvService.getProvinceById(provinceId));
		}
		if (!cityId.equals("0")) {
			newUser.setCity(ctService.getCityById(cityId));
		}
		if (!collegeId.equals("0")) {
			newUser.setCollege(clgService.getCollegeById(collegeId));
		}
		if (interests != null) {
			List<Interest> interestList = new ArrayList<Interest>();
			for (String interestName : interests) {
				Interest interest = intrService.getInterestByName(interestName);
				if (interest != null) {
					interestList.add(interest);
				}
				// add new interest into data
				else {
					Interest newInterest = new Interest(interestName);
					intrService.save(newInterest);
				}
			}
			newUser.setInterests(interestList);
		}
		
		uService.save(newUser);
		
		// then login
		doLogin(model, email, password, true, session, request, response);
		
		result.put("code", 1);
		
		return result;
	}
	
	@RequestMapping("login")
	public String login(ModelMap model) {
		
		return "login";
	}
	
	@RequestMapping(value = "login.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doLogin(ModelMap model,
									   String email,
									   String password,
									   boolean rememberme,
									   HttpSession session,
									   HttpServletRequest request,
									   HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// remove old cookie first
		User currentUser = (User) session.getAttribute(USER_SESSION);
		if (currentUser != null) {
			CookieUtil.removeCookie(request, response, CookieUtil.USER_COOKIE);
		}
		
		// get login user by email
		User loginUser = uService.getUserByEmail(email);
		// email not used, or wrong password
		if (loginUser == null || !MD5Util.authenticateInputPassword(loginUser.getPassword(), password)) {
			result.put("code", -1);
			return result;
		}
		
		// add user session
		model.addAttribute(USER_SESSION, loginUser);
		// add user cookie
		if (rememberme) {
			response.addCookie(CookieUtil.generateUserCookie(loginUser));
		}
		
		// update lastLoginDate
		loginUser.setLastLoginDate(new java.sql.Date(new java.util.Date().getTime()));
		uService.update(loginUser);
		
		result.put("code", 1);
		
		return result;
	}
	
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> logoutUser(SessionStatus sessionStatus,
										  HttpServletRequest request,
										  HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		// remove session
		sessionStatus.setComplete();
		// remove cookie
		CookieUtil.removeCookie(request, response, CookieUtil.USER_COOKIE);
		
		return result;
	}
	
	@RequestMapping("c/{clubId}")
	public String clubPage(ModelMap model,
						   HttpSession session,
						   @PathVariable String clubId) {
		Club club = cService.getClubById(clubId);
		if (club == null) {
			return "STATIC/404";
		}
		model.put("club", club);
		List<Group> groups = gpService.getClubGroupList(club.getId());
		model.put("defaultGroup", groups.get(0));
		groups.remove(groups.size()-1);
		if (groups.size() > 0) {
			Group loadedGroup = groups.get(0);
			model.put("loadedGroup", loadedGroup);
			model.put("clubMemberList", cmService.getClubMemberListByGroupId(groups.get(0).getId()));
		}
		model.put("groupList", groups);
		model.put("activityList", actService.getFinishedActivityListByClubId(clubId));
		model.put("memberCount", cmService.getClubMemberListByClubId(clubId).size());
		model.put("activityCount", actService.getFinishedActivityListByClubId(clubId).size());
		model.put("subscriptionCount", subService.getSubscriptionListByClubId(clubId).size());
		
		User currentUser = (User) session.getAttribute(USER_SESSION);
		if (currentUser != null) {
			model.put("isSubscribed", subService.getSubscriptionByUserIdAndClubId(currentUser.getId(), club.getId()) != null);
		}
		
		return "CLUB/homepage";
	}
	
	@RequestMapping("no_permission")
	public String noPermission() {
		return "STATIC/no_permission";
	}
	
}
