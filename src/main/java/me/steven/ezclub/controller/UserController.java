package me.steven.ezclub.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import me.steven.ezclub.entity.Activity;
import me.steven.ezclub.entity.Club;
import me.steven.ezclub.entity.ClubMember;
import me.steven.ezclub.entity.Interest;
import me.steven.ezclub.entity.Message;
import me.steven.ezclub.entity.Subscription;
import me.steven.ezclub.entity.User;
import me.steven.ezclub.service.ActivityService;
import me.steven.ezclub.service.CityService;
import me.steven.ezclub.service.ClubMemberService;
import me.steven.ezclub.service.ClubService;
import me.steven.ezclub.service.CollegeService;
import me.steven.ezclub.service.ContentService;
import me.steven.ezclub.service.DepartmentService;
import me.steven.ezclub.service.GroupService;
import me.steven.ezclub.service.InterestService;
import me.steven.ezclub.service.MessageService;
import me.steven.ezclub.service.ProvinceService;
import me.steven.ezclub.service.SubscriptionService;
import me.steven.ezclub.service.UserService;
import me.steven.ezclub.util.MD5Util;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService uService;
	@Autowired
	private ClubService cService;
	@Autowired
	private ClubMemberService cmService;
	@Autowired
	private GroupService gpService;
	@Autowired
	private DepartmentService deptService;
	@Autowired
	private ProvinceService pvService;
	@Autowired
	private CityService ctService;
	@Autowired
	private CollegeService clgService;
	@Autowired
	private InterestService intrService;
	@Autowired
	private ActivityService actService;
	@Autowired
	private ContentService conService;
	@Autowired
	private SubscriptionService subService;
	@Autowired
	private MessageService msgService;
	
	@RequestMapping("")
	public String index(ModelMap model) {
		
		return "USER/index";
	}
	
	@RequestMapping("subscription")
	public String mySubscriptions(ModelMap model,
								  HttpSession session) {
		User currentUser = (User) session.getAttribute("USER_SESSION");
		List<Subscription> subscriptionList = subService.getSubscriptionListByUserId(currentUser.getId());
		if (subscriptionList.size() > 0) {
			// redirect to the first one
			return "redirect:../user/subscription/" + subscriptionList.get(0).getClub().getId();
		}
		return "USER/my_subscription";
	}

	@RequestMapping("subscription/{clubId}")
	public String mySubscriptions(ModelMap model,
								  HttpSession session,
								  @PathVariable String clubId) {
		User currentUser = (User) session.getAttribute("USER_SESSION");
		Club club = cService.getClubById(clubId);
		
		if (club == null) {
			return "STATIC/404";
		}
		
		Subscription subscription = subService.getSubscriptionByUserIdAndClubId(currentUser.getId(), club.getId());
		
		if (subscription == null) {
			return "STATIC/404";
		}
		
		model.put("subscription", subscription);
		model.put("mySubscriptionList", subService.getSubscriptionListByUserId(currentUser.getId()));
		model.put("unreadActivityList", subscription.getUnreadActivities());
		model.put("allActivityList", actService.getFinishedActivityListByClubId(club.getId()));
		
		// clear unread list
		subscription.setUnreadActivities(new ArrayList<Activity>());
		subService.update(subscription);
		
		return "USER/my_subscription";
	}
	
	@RequestMapping("club")
	public String myClubs(ModelMap model,
						  HttpSession session) {
		User currentUser = (User) session.getAttribute("USER_SESSION");
		List<ClubMember> myClubMemberList = cmService.getAvailableClubMemberListByUserId(currentUser.getId());
		if (myClubMemberList.size() > 0) {
			// redirect to the first one
			return "redirect:../user/club/" + myClubMemberList.get(0).getClub().getId();
		}
		return "USER/my_club";
	}
	
	@RequestMapping("club/{clubId}")
	public String myClub(ModelMap model,
						 HttpSession session,
						 @PathVariable String clubId) {
		User currentUser = (User) session.getAttribute("USER_SESSION");
		Club club = cService.getClubById(clubId);
		
		if (club == null) {
			return "STATIC/404";
		}
		
		ClubMember clubMember = cmService.getClubMemberByUserIdAndClubId(currentUser.getId(), club.getId());
		if (clubMember == null) {				// not a member
			return "STATIC/404";
		}
		
		model.put("club", club);
		model.put("myClubMember", clubMember);
		model.put("myClubMemberList", cmService.getAvailableClubMemberListByUserId(currentUser.getId()));
		model.put("groupClubMemberList", cmService.getClubMemberListByGroupId(clubMember.getGroup().getId()));
		model.put("departmentList", deptService.getDepartmentListByClubId(club.getId()));
		model.put("defaultMembers", cmService.getClubMemberListByGroupId(gpService.getClubDefaultGroup(club.getId()).getId()));
		
		List<Activity> activityList = actService.getAllActivityListByClubId(club.getId());
		for (Activity act : activityList) {
			act.getData().put("textContentAmount", conService.getContentListByActivityIdAndType(act.getId(), 1).size());
			act.getData().put("imageContentAmount", conService.getContentListByActivityIdAndType(act.getId(), 2).size());
			act.getData().put("videoContentAmount", conService.getContentListByActivityIdAndType(act.getId(), 3).size());
		}
		model.put("activityList", activityList);
		
		return "USER/my_club";
	}
	
	@RequestMapping("message")
	public String myMessages(ModelMap model,
						 HttpSession session) {
		User currentUser = (User) session.getAttribute("USER_SESSION");
		
		model.put("unreadMessageList", msgService.getUnreadMessageListByAddresseeUserId(currentUser.getId()));
		model.put("readMessageList", msgService.getReadMessageListByAddresseeUserId(currentUser.getId()));
		return "USER/my_message";
	}
	
	@RequestMapping(value = "update_member_info", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateMemberInfo(String cmid,
												String name,
												String contact) {
		Map<String, Object> result = new HashMap<String, Object>();
		ClubMember clubMember = cmService.getClubMemberById(cmid);
		
		if (clubMember == null) {
			result.put("code", 0);
			return result;
		}
		
		clubMember.setName(name);
		clubMember.setContact(contact);
		cmService.update(clubMember);
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping(value = "read_message", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> readMessage(HttpSession session,
										  String messageId) {
		Map<String, Object> result = new HashMap<String, Object>();
		User currentUser = (User) session.getAttribute("USER_SESSION");
		Message message = msgService.getMessageById(messageId);
		
		if (message == null) {
			result.put("code", 0);
			return result;
		}
		
		if (!message.getAddressee().getId().equals(currentUser.getId())) {
			result.put("code", -1);		// only the addressee can do that
			return result;
		}
		
		message.setIsRead(true);
		msgService.update(message);
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping(value = "delete_message", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteMessage(HttpSession session,
											 String messageId) {
		Map<String, Object> result = new HashMap<String, Object>();
		User currentUser = (User) session.getAttribute("USER_SESSION");
		Message message = msgService.getMessageById(messageId);
		
		if (message == null) {
			result.put("code", 0);
			return result;
		}
		
		if (!message.getAddressee().getId().equals(currentUser.getId())) {
			result.put("code", -1);		// only the addressee can do that
			return result;
		}
		
		msgService.delete(message);
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping("setting")
	public String setting(ModelMap model,
						 HttpSession session) {
		User currentUser = (User) session.getAttribute("USER_SESSION");
		model.put("user", currentUser);
		model.put("provinceList", pvService.getProvinceList());
		if (currentUser.getProvince() != null) {
			model.put("cityList", ctService.getCityListByProvinceId(currentUser.getProvince().getId()));
		}
		if (currentUser.getCity() != null) {
			model.put("collegeList", clgService.getAvailableCollegeListByCityId(currentUser.getCity().getId()));
		}
		return "USER/setting";
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateUser(String nickname,
										  int sex,
										  String birth,
										  String provinceId,
										  String cityId,
										  String collegeId,
										  @RequestParam(value="interests[]", required=false) String[] interests,
										  HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		User currentUser = (User) session.getAttribute("USER_SESSION");
		
		currentUser.setNickname(nickname);
		currentUser.setSex(sex);
		if (!birth.equals("")) {
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date = format.parse(birth);
				java.sql.Date birthDate = new java.sql.Date(date.getTime());
				currentUser.setBirthDate(birthDate);
			} catch (ParseException e) {
				System.out.println(e.getMessage());
			}
		}
		else {
			currentUser.setBirthDate(null);
		}
		if (!provinceId.equals("0")) {
			currentUser.setProvince(pvService.getProvinceById(provinceId));
		}
		else {
			currentUser.setProvince(null);
		}
		if (!cityId.equals("0")) {
			currentUser.setCity(ctService.getCityById(cityId));
		}
		else {
			currentUser.setCity(ctService.getCityById(null));
		}
		if (!collegeId.equals("0")) {
			currentUser.setCollege(clgService.getCollegeById(collegeId));
		}
		else {
			currentUser.setCollege(null);
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
			currentUser.setInterests(interestList);
		}
		else {
			currentUser.setInterests(null);
		}
		
		uService.update(currentUser);
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping(value = "upload_portrait", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadPortrait(HttpSession session,
											  HttpServletRequest request,
											  String fileType,
											  MultipartFile portraitFile) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		User currentUser = (User) session.getAttribute("USER_SESSION");
		
		String saveName = "images/portrait/" + currentUser.getId() + fileType;
		String savePath = request.getServletContext().getRealPath("") + "/WEB-INF/classes/" + saveName;
		try {
			File file = new File(portraitFile.getOriginalFilename());
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(portraitFile.getBytes());
			fos.close();
			FileUtils.copyFile(file, new File(savePath));
			result.put("newPhoto", saveName);
		} catch (IOException e) {
			e.printStackTrace();
			return result;
		}
		
		currentUser.setPhoto(saveName);
		uService.update(currentUser);
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping(value = "quit_club", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> quitClub(HttpSession session,
										String clubId,
										String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		Club club = cService.getClubById(clubId);
		if (club == null) {
			result.put("code", 0);			// wrong clubId
			return result;
		}
		
		User currentUser = (User) session.getAttribute("USER_SESSION");
		
		if (!MD5Util.authenticateInputPassword(currentUser.getPassword(), password)) {
			result.put("code", -1);			// wrong password
			return result;
		}
		
		ClubMember clubMember = cmService.getClubMemberByUserIdAndClubId(currentUser.getId(), clubId);
		if (clubMember == null) {
			result.put("code", -2);			// not a member
			return result;
		}
		
		cmService.delete(clubMember);
		
		result.put("code", 1);
		return result;
	}
	
}
