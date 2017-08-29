package me.steven.ezclub.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.steven.ezclub.entity.Club;
import me.steven.ezclub.entity.College;
import me.steven.ezclub.entity.Message;
import me.steven.ezclub.entity.Subscription;
import me.steven.ezclub.service.ActivityService;
import me.steven.ezclub.service.ClubMemberService;
import me.steven.ezclub.service.ClubService;
import me.steven.ezclub.service.CollegeService;
import me.steven.ezclub.service.MessageService;
import me.steven.ezclub.service.SubscriptionService;
import me.steven.ezclub.service.UserService;
import me.steven.ezclub.util.MessageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("admin")
public class AdminController {

	@Autowired
	private UserService uService;
	@Autowired
	private ClubService cService;
	@Autowired
	private ClubMemberService cmService;
	@Autowired
	private CollegeService clgService;
	@Autowired
	private MessageService msgService;
	@Autowired
	private ActivityService actService;
	@Autowired
	private SubscriptionService subService;
	@Autowired
	private SimpMessagingTemplate template;
	
	@RequestMapping("college")
	public String college(ModelMap model) {
		List<College> uncheckedCollegeList = clgService.getUncheckedCollegeList();
		List<College> checkedCollegeList = clgService.getCheckedCollegeList();
		// insert extra data
		for (College college : checkedCollegeList) {
			college.getData().put("clubCount", cService.getClubListByCollegeId(college.getId()).size());
		}
		model.put("uncheckedCollegeList", uncheckedCollegeList);
		model.put("checkedCollegeList", checkedCollegeList);
		model.put("uncheckedAmount", uncheckedCollegeList.size());
		return "ADMIN/college";
	}
	
	@RequestMapping(value = "update_college.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> do_check_college(String clgid,
												boolean isPass,
												String cnName,
												String enName,
												String shortName,
												String intro) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		College college = clgService.getCollegeById(clgid);
		if (college == null) {
			result.put("code", -2);		// wrong collegeId
			return result;
		}
		College clg = clgService.getCollegeByCnName(cnName);
		if (clg != null && !clg.getId().equals(clgid)) {
			result.put("code", -1);		// conflicted cnName
			return result;
		}
		
		if (isPass) {
			college.setStatus(1);		// pass
		}
		else {
			college.setStatus(-1);		// hide
		}
		college.setCnName(cnName);
		college.setEnName(enName);
		college.setShortName(shortName);
		college.setIntro(intro);
		
		clgService.update(college);
		result.put("code", 1);			// succeed
		
		return result;
	}
	
	@RequestMapping("club")
	public String club(ModelMap model) {
		List<Club> uncheckedClubList = cService.getUncheckedClubList();
		List<Club> checkedClubList = cService.getCheckedClubList();
		for (Club club : checkedClubList) {
			club.getData().put("memberCount", cmService.getClubMemberListByClubId(club.getId()).size());
			club.getData().put("activityCount", actService.getFinishedActivityListByClubId(club.getId()).size());
			club.getData().put("subscriptionCount", subService.getSubscriptionListByClubId(club.getId()).size());
		}
		model.put("uncheckedClubList", uncheckedClubList);
		model.put("checkedClubList", checkedClubList);
		model.put("uncheckedAmount", uncheckedClubList.size());
		return "ADMIN/club";
	}
	
	
	@RequestMapping(value = "update_club.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> do_check_club(String cid,
											 boolean isPass,
											 String cnName,
											 String enName,
											 String intro) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		Club club = cService.getClubById(cid);
		
		Club c = cService.getClubByCnName(cnName);
		if (c != null && !c.getId().equals(cid)) {
			result.put("code", -1);		// conflicted cnName
			return result;
		}
		
		if (isPass) {
			club.setStatus(1);		// pass
			// manager subscribe the club by default
			Subscription subscription = new Subscription(club.getManager().getUser(), club);
			subService.save(subscription);
		}
		else {
			club.setStatus(-1);		// hide
		}
		club.setCnName(cnName);
		club.setEnName(enName);
		club.setIntro(intro);
		cService.update(club);
		
		// send an anonymous message from system to the club creator
		Message message = new Message(null, club.getManager().getUser(), MessageUtil.newClubCheckedNotify(club, null));
		msgService.save(message);
		result.put("unreadMessageCount", msgService.getUnreadMessageListByAddresseeUserId(club.getManager().getUser().getId()).size());
		template.convertAndSend("queue/message/" + club.getManager().getUser().getId(), result);
		
		result.put("code", 1);			// succeed
		
		return result;
	}
	
}
