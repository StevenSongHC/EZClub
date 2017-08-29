package me.steven.ezclub.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import me.steven.ezclub.entity.Activity;
import me.steven.ezclub.entity.Club;
import me.steven.ezclub.entity.ClubMember;
import me.steven.ezclub.entity.Department;
import me.steven.ezclub.entity.Group;
import me.steven.ezclub.entity.Message;
import me.steven.ezclub.entity.Subscription;
import me.steven.ezclub.entity.Tag;
import me.steven.ezclub.entity.User;
import me.steven.ezclub.service.ActivityService;
import me.steven.ezclub.service.ClubMemberService;
import me.steven.ezclub.service.ClubService;
import me.steven.ezclub.service.ContentService;
import me.steven.ezclub.service.DepartmentService;
import me.steven.ezclub.service.GroupService;
import me.steven.ezclub.service.MessageService;
import me.steven.ezclub.service.SubscriptionService;
import me.steven.ezclub.service.TagService;
import me.steven.ezclub.service.UserService;
import me.steven.ezclub.util.MD5Util;
import me.steven.ezclub.util.MessageUtil;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("club")
public class ClubController {

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
	private TagService tagService;
	@Autowired
	private MessageService msgService;
	@Autowired
	private ActivityService actService;
	@Autowired
	private ContentService conService;
	@Autowired
	private SubscriptionService subService;
	@Autowired
	private SimpMessagingTemplate template;
	
	@RequestMapping("load_group")
	@ResponseBody
	public Map<String, Object> loadGroup(String clubId,
										 int year) {
		Map<String, Object> result = new HashMap<String, Object>();
		Group group = gpService.getGroupByClubIdAndYear(clubId, year);
		if (group == null) {
			return result;
		}
		
		// can't just parse object to JSON because of such redundant data
		result.put("groupId", group.getId());
		result.put("wholePhoto", group.getWholePhoto());
		result.put("newbiePhoto", group.getNewbiePhoto());
		
		List<Map<String, Object>> clubMembers = new ArrayList<Map<String, Object>>();
		for(ClubMember clubMember : cmService.getClubMemberListByGroupId(group.getId())) {
			Map<String, Object> member = new HashMap<String, Object>();
			member.put("clubMemberId", clubMember.getId());
			member.put("clubMemberName", clubMember.getName());
			member.put("departmentId", clubMember.getDepartment().getId());
			member.put("userNickname", clubMember.getUser().getNickname());
			member.put("name", clubMember.getName());
			member.put("contact", clubMember.getContact());
			member.put("isRestricted", clubMember.getUser().getStatus() != 1);
			member.put("isManager", clubMember.getId().equals(group.getClub().getManager().getId()));
			clubMembers.add(member);
		}
		result.put("clubMembers", clubMembers);
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping("manage/{clubId}")
	public String manageClub(HttpSession session,
							 ModelMap model,
							 @PathVariable String clubId) {
		Club club = cService.getClubById(clubId);
		if (club == null) {
			return "STATIC/404";
		}
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (!currentUser.getId().equals(club.getManager().getUser().getId())) {
			return "STATIC/no_permission";
		}
		
		model.put("club", club);
		model.put("memberCount", cmService.getClubMemberListByClubId(clubId).size());
		model.put("activityCount", actService.getFinishedActivityListByClubId(clubId).size());
		model.put("subscriptionCount", subService.getSubscriptionListByClubId(clubId).size());
		
		List<Group> groups = gpService.getClubGroupList(club.getId());
		// remove the default group
		groups.remove(groups.size()-1);
		model.put("groupList", groups);
		// load all clubMembers of the loaded group
		if (groups.size() > 0) {
			Group loadedGroup = groups.get(0);
			model.put("loadedGroup", loadedGroup);
			model.put("clubMemberList", cmService.getClubMemberListByGroupId(groups.get(0).getId()));
		}
		// load all departments
		model.put("departmentList", deptService.getDepartmentListByClubId(club.getId()));
		// default group's members
		model.put("defaultMembers", cmService.getClubMemberListByGroupId(gpService.getClubDefaultGroup(club.getId()).getId()));
		
		return "CLUB/manage";
	}
	
	@RequestMapping(value = "manage/update_club", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateClub(HttpSession session,
										  String clubId,
										  int type,
										  @RequestParam(value="cnName", required=false) String cnName,
										  @RequestParam(value="enName", required=false) String enName,
										  @RequestParam(value="intro", required=false) String intro,
										  @RequestParam(value="isAddTag", required=false) boolean isAddTag,
										  @RequestParam(value="tag", required=false) String tag) {
		Map<String, Object> result = new HashMap<String, Object>();
		Club club = cService.getClubById(clubId);
		if (club == null) {
			result.put("code", 0);			// wrong clubId
			return result;
		}
		
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (!currentUser.getId().equals(club.getManager().getUser().getId())) {
			result.put("code", -1);				// no permission
			return result;
		}
		
		switch(type) {
			case 1:							// update name
				club.setCnName(cnName);
				club.setEnName(enName);
				break;
			case 2:							// update intro
				club.setIntro(intro);
				break;
			case 3:							// update tags
				// if isAddTag=true -> tag=tag.name, otherwise tag=tag.id
				if (isAddTag) {				// add new tag to club's record
					Tag t = tagService.getTagByName(tag);
					if (t==null) {			// new tag record
						t = new Tag(tag);
						tagService.save(t);
					}
					else {					// check whether already had such tag
						Iterator<Tag> iter = club.getTags().iterator();
						while (iter.hasNext()) {
							if (iter.next().getName().equals(tag)) {
								result.put("code", 2);	// tag already existed in the list of tags
								System.out.println("COCNCNC");
								return result;
							}
						}
					}
					club.getTags().add(t);	// add
					result.put("newTag", t);
				}
				else {						// remove tag from club's record
					Iterator<Tag> iter = club.getTags().iterator();
					while (iter.hasNext()) {
						if (iter.next().getId().equals(tag)) {
							iter.remove();
							break;			// stop the loop once found the matched tag
						}
					}
				}
				break;
			default:
				result.put("code", -2);		// unspecified updating type
				return result;
		}
		cService.update(club);
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping(value = "manage/upload_badge", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadBadge(HttpSession session,
										   HttpServletRequest request,
										   String clubId,
										   String fileType,
										   MultipartFile badgeFile) {
		Map<String, Object> result = new HashMap<String, Object>();
		Club club = cService.getClubById(clubId);
		if (club == null) {
			result.put("code", 0);			// wrong clubId
			return result;
		}
		
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (!currentUser.getId().equals(club.getManager().getUser().getId())) {
			result.put("code", -1);				// no permission
			return result;
		}
		
		String saveName = "images/club-badge/" + club.getId() + fileType;
		String savePath = request.getServletContext().getRealPath("") + "/WEB-INF/classes/" + saveName;
		// save badge file
		try {
			/*badgeFile.transferTo(new File(savePath));*/		// this method cannot be invoked twice, hence not using it
			File file = new File(badgeFile.getOriginalFilename());
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(badgeFile.getBytes());
			fos.close();
			FileUtils.copyFile(file, new File(savePath));
			result.put("newBadge", saveName);
		} catch (IOException e) {
			e.printStackTrace();
			return result;
		}
		
		club.setBadge(saveName);
		cService.update(club);
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping(value = "manage/upload_photo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadphoto(HttpSession session,
										   HttpServletRequest request,
										   String groupId,
										   String type,
										   String fileType,
										   MultipartFile photoFile) {
		Map<String, Object> result = new HashMap<String, Object>();
		Group group = gpService.getGroupById(groupId);
		if (group == null) {
			result.put("code", 0);			// wrong groupId
			return result;
		}
		
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (!currentUser.getId().equals(group.getClub().getManager().getUser().getId())) {
			result.put("code", -1);				// no permission
			return result;
		}
		System.out.println(type);
		if (!type.equals("whole") && !type.equals("newbie")) {
			result.put("code", -2);			// wrong type
			return result;
		}
		
		String saveName = "images/club-group/" + type + "/" + group.getId() + fileType;
		String savePath = request.getServletContext().getRealPath("") + "/WEB-INF/classes/" + saveName;
		System.out.println(savePath);
		// save badge file
		try {
			File file = new File(photoFile.getOriginalFilename());
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(photoFile.getBytes());
			fos.close();
			FileUtils.copyFile(file, new File(savePath));
			result.put("newPhoto", saveName);
		} catch (IOException e) {
			e.printStackTrace();
			return result;
		}
		
		// already check safety before
		if (type.equals("whole")) {
			group.setWholePhoto(saveName);
		}
		else {
			group.setNewbiePhoto(saveName);
		}
		gpService.update(group);
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping(value = "manage/add_department", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addDepartment(HttpSession session,
											 String clubId,
											 String title) {
		Map<String, Object> result = new HashMap<String, Object>();
		Club club = cService.getClubById(clubId);
		if (club == null) {
			result.put("code", 0);			// wrong clubId
			return result;
		}
		
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (!currentUser.getId().equals(club.getManager().getUser().getId())) {
			result.put("code", -1);				// no permission
			return result;
		}
		
		Department department = new Department(title, club);
		deptService.save(department);		// create
		
		result.put("newDepartmentId", department.getId());
		result.put("newDepartmentTitle", department.getTitle());
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping(value = "manage/add_club_member", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addClubMember(HttpSession session,
											 String clubId,
											 String nickname,
											 int year,
											 String departmentId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Club club = cService.getClubById(clubId);
		if (club == null) {
			result.put("code", 0);			// wrong clubId
			return result;
		}
		
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (!currentUser.getId().equals(club.getManager().getUser().getId())) {
			result.put("code", -1);				// no permission
			return result;
		}
		
		User user = uService.getUserByNickname(nickname);
		if (user == null) {
			result.put("code", -2);			// nickname failed
			return result;
		}
		
		ClubMember clubMember;
		
		if (departmentId.equals("0")) {		// add to default group
			Group defaultGroup = gpService.getClubDefaultGroup(club.getId());
			clubMember = cmService.getClubMemberByUserIdAndClubId(user.getId(), club.getId());
			// add member
			if (clubMember == null) {
				clubMember = new ClubMember(user.getNickname(), user, club, defaultGroup);
				cmService.save(clubMember);

				// subscribe the club by default
				Subscription subscription = new Subscription(clubMember.getUser(), club);
				subService.save(subscription);
			}
			// move member
			else {
				Group  oldGroup = clubMember.getGroup();
				clubMember.setGroup(defaultGroup);
				clubMember.setDepartment(null);
				clubMember.setJoinDate(new java.sql.Date(new java.util.Date().getTime()));
				cmService.update(clubMember);
				// delete group if there is no member in that group anymore, and you can't delete the default group
				if (cmService.getClubMemberListByGroupId(oldGroup.getId()).size() == 0 
						&& !oldGroup.getId().equals(defaultGroup.getId())) {
					gpService.delete(oldGroup);
					result.put("isDeleteGroup", true);
				}
			}
			result.put("isDefault", true);
			result.put("clubMemberId", clubMember.getId());
			result.put("groupId", defaultGroup.getId());
		}
		else {								// add to defined group
			Department department = deptService.getDepartmentById(departmentId);
			if (department == null) {
				result.put("code", -3);		// wrong departmentId
				return result;
			}
			Group group = gpService.getGroupByClubIdAndYear(club.getId(), year);
			if (group == null) {			// create group if not existed such one
				group = new Group(club, year);
				gpService.save(group);
				result.put("isNewGroup", true);
			}
			clubMember = cmService.getClubMemberByUserIdAndClubId(user.getId(), club.getId());
			if (clubMember == null) {
				clubMember = new ClubMember(user.getNickname(), user, club, group);
				clubMember.setDepartment(department);
				cmService.save(clubMember);

				Subscription subscription = new Subscription(clubMember.getUser(), club);
				subService.save(subscription);
			}
			else {
				Group  oldGroup = clubMember.getGroup();
				clubMember.setGroup(group);
				clubMember.setDepartment(department);
				cmService.update(clubMember);
				if (cmService.getClubMemberListByGroupId(oldGroup.getId()).size() == 0
						&& !oldGroup.getId().equals(gpService.getClubDefaultGroup(club.getId()).getId())) {
					gpService.delete(oldGroup);
					result.put("isDeleteGroup", true);
				}
			}
			result.put("departmentId", department.getId());
			result.put("clubMemberId", clubMember.getId());
			result.put("groupId", group.getId());
		}

		// send message
		Message message = new Message(null, user, MessageUtil.clubMemberAddedNotify(clubMember));
		msgService.save(message);
		result.put("unreadMessageCount", msgService.getUnreadMessageListByAddresseeUserId(user.getId()).size());
		template.convertAndSend("queue/message/" + user.getId(), result);
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping(value = "manage/change_manager", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> changeManager(HttpSession session,
											 String clubId,
											 String newManagerId,
											 String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		Club club = cService.getClubById(clubId);
		if (club == null) {
			result.put("code", 0);			// wrong clubId
			return result;
		}
		
		ClubMember oldManager = club.getManager();
		
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (!currentUser.getId().equals(oldManager.getUser().getId())) {
			result.put("code", -1);			// no permission
			return result;
		}
		
		if (!MD5Util.authenticateInputPassword(oldManager.getUser().getPassword(), password)) {
			result.put("code", -3);			// wrong password
			return result;
		}
		
		ClubMember newManager = cmService.getClubMemberById(newManagerId);
		if (newManager == null) {
			result.put("code", -2);			// wrong clubMemberId
			return result;
		}
		
		if (newManager.getId().equals(oldManager.getId())) {
			result.put("code", 1);			// if it's the same manager, then do nothing
			return result;
		}
		
		club.setManager(newManager);
		cService.update(club);
		
		// send message
		Message message = new Message(null, newManager.getUser(), MessageUtil.becomeManagerNotify(oldManager));
		msgService.save(message);
		result.put("unreadMessageCount", msgService.getUnreadMessageListByAddresseeUserId(newManager.getUser().getId()).size());
		template.convertAndSend("queue/message/" + newManager.getUser().getId(), result);
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping(value = "add_activity", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> quitClub(HttpSession session,
										String clubId,
										String title) {
		Map<String, Object> result = new HashMap<String, Object>();
		Club club = cService.getClubById(clubId);
		if (club == null) {
			result.put("code", 0);			// wrong clubId
			return result;
		}
		
		User currentUser = (User) session.getAttribute("USER_SESSION");
		
		if (!club.getManager().getUser().getId().equals(currentUser.getId())) {
			result.put("code", -1);			// no permission
			return result;
		}
		
		if (actService.getActivityByClubIdAndTitle(club.getId(), title) != null) {
			result.put("code", -2);			// existed title
			return result;
		}
		
		Activity newActivity = new Activity(title, club);
		actService.save(newActivity);
		
		result.put("newActivityId", newActivity.getId());
		result.put("newActivityTitle", newActivity.getTitle());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		result.put("newActivityUpdateDate", sdf.format(newActivity.getUpdateDate()));
		
		result.put("code", 1);
		return result;
	}
	

	@RequestMapping(value = "subscribe", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> subscribeClub(HttpSession session, 
											 String clubId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Club club = cService.getClubById(clubId);
		if (club == null) {
			result.put("code", 0);			// wrong clubId
			return result;
		}
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (currentUser == null) {
			result.put("code", -1);			// need login
			return result;
		}
		if (subService.getSubscriptionByUserIdAndClubId(currentUser.getId(), club.getId()) != null) {
			result.put("code", -2);			// already subscribed
		}
		
		Subscription subscription = new Subscription(currentUser, club);
		subService.save(subscription);
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping(value = "unsubscribe", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> unsubscribeClub(HttpSession session, 
											   String clubId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Club club = cService.getClubById(clubId);
		if (club == null) {
			result.put("code", 0);			// wrong clubId
			return result;
		}
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (currentUser == null) {
			result.put("code", -1);			// need login
			return result;
		}
		Subscription subscription = subService.getSubscriptionByUserIdAndClubId(currentUser.getId(), club.getId());
		if (subscription == null) {
			result.put("code", -2);			// need to subscribe first
		}
		
		subService.delete(subscription);
		
		result.put("code", 1);
		return result;
	}
	
}
