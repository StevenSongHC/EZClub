package me.steven.ezclub.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import me.steven.ezclub.entity.Activity;
import me.steven.ezclub.entity.ClubMember;
import me.steven.ezclub.entity.Comment;
import me.steven.ezclub.entity.Content;
import me.steven.ezclub.entity.Subscription;
import me.steven.ezclub.entity.User;
import me.steven.ezclub.service.ActivityService;
import me.steven.ezclub.service.ClubMemberService;
import me.steven.ezclub.service.ClubService;
import me.steven.ezclub.service.CommentService;
import me.steven.ezclub.service.ContentService;
import me.steven.ezclub.service.SubscriptionService;
import me.steven.ezclub.service.UserService;

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
@RequestMapping("activity")
public class ActivityController {

	@Autowired
	private UserService uService;
	@Autowired
	private ClubService cService;
	@Autowired
	private ClubMemberService cmService;
	@Autowired
	private ActivityService actService;
	@Autowired
	private ContentService conService;
	@Autowired
	private SubscriptionService subService;
	@Autowired
	private CommentService cmtService;
	@Autowired
	private SimpMessagingTemplate template;
	
	@RequestMapping("{activityId}")
	public String browseActivity(HttpSession session,
							 	 ModelMap model,
							 	 @PathVariable String activityId) {
		Activity activity = actService.getActivityById(activityId);
		if (activity == null || !activity.getIsFinished()) {
			return "STATIC/404";
		}
		
		model.put("activity", activity);
		model.put("contentList", conService.getContentListByActivityId(activity.getId()));
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (currentUser != null) {
			model.put("isSubscribed", subService.getSubscriptionByUserIdAndClubId(currentUser.getId(), activity.getClub().getId()) != null);
		}
		model.put("commentList", cmtService.getCommentListByActivityId(activity.getId()));
		return "ACTIVITY/browse";
	}
	
	@RequestMapping("edit/{activityId}")
	public String editActivity(HttpSession session,
							   ModelMap model,
							   @PathVariable String activityId) {
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (currentUser == null) {
			return "redirect:../../login";
		}
		
		Activity activity = actService.getActivityById(activityId);
		if (activity == null) {
			return "STATIC/404";
		}
		
		// only member can edit
		ClubMember clubMember = cmService.getClubMemberByUserIdAndClubId(currentUser.getId(), activity.getClub().getId());
		if (clubMember == null) {
			return "STATIC/no_permission";
		}
		
		// redirect to the activity page if been published
		if (activity.getIsFinished()) {
			return "redirect:../" + activity.getId();
		}
		
		boolean isEditorExisted = false;
		for (ClubMember cm : activity.getEditors()) {
			if (cm.getId().equals(clubMember.getId())) {
				isEditorExisted = true;
			}
		}
		if (!isEditorExisted) {
			// add to editor list
			activity.getEditors().add(clubMember);
			actService.update(activity);
			// send a notify
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("editorId", clubMember.getId());
			data.put("editorName", clubMember.getName());
			data.put("editorContact", clubMember.getContact());
			data.put("editorAmount", activity.getEditors().size());
			data.put("type", 1);				// new editor entered
			template.convertAndSend("topic/edit_activity/" + activity.getId(), data);
		}
		
		model.put("clubMember", clubMember);
		model.put("activity", activity);
		model.put("myContentList", conService.getContentListByClubMemberIdAndActivityId(clubMember.getId(), activity.getId()));
		model.put("contentList", conService.getContentListByActivityId(activity.getId()));
		
		return "ACTIVITY/edit";
	}
	
	@RequestMapping(value = "create_text_content", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createTextContent(HttpSession session,
												 String activityId,
												 String content) {
		Map<String, Object> result = new HashMap<String, Object>();
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (currentUser == null) {
			result.put("code", -1);		// need login
			return result;
		}
		Activity activity = actService.getActivityById(activityId);
		if (activity == null) {
			result.put("code", 0);		// wrong activityId
			return result;
		}
		ClubMember creator = cmService.getClubMemberByUserIdAndClubId(currentUser.getId(), activity.getClub().getId());
		if (creator == null) {
			result.put("code", -2);		// not a member
			return result;
		}
		
		Content textContent = new Content(content, 1, activity, creator);
		conService.save(textContent);
		
		// update activity's update date
		activity.setUpdateDate(textContent.getSubmitDate());
		actService.update(activity);
		
		result.put("contentId", textContent.getId());
		result.put("content", textContent.getContent());
		result.put("editorName", creator.getName());
		result.put("editorPhoto", creator.getUser().getPhoto());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		result.put("editingDate", sdf.format(textContent.getSubmitDate()));

		result.put("type", 4);				// new text content created
		template.convertAndSend("topic/edit_activity/" + activity.getId(), result);
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping(value = "create_rich_content", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createRichContent(HttpSession session,
												 HttpServletRequest request,
												 String activityId,
												 int type,
												 String content,
												 String fileType,
												 @RequestParam(value="imageFile", required=false) MultipartFile imageFile,
												 @RequestParam(value="videoFile", required=false) MultipartFile videoFile) {
		Map<String, Object> result = new HashMap<String, Object>();
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (currentUser == null) {
			result.put("code", -1);		// need login
			return result;
		}
		Activity activity = actService.getActivityById(activityId);
		if (activity == null) {
			result.put("code", 0);		// wrong activityId
			return result;
		}
		ClubMember clubMember = cmService.getClubMemberByUserIdAndClubId(currentUser.getId(), activity.getClub().getId());
		if (clubMember == null) {
			result.put("code", -2);		// not a member
			return result;
		}
		
		String saveName = "";
		String savePath = "";
		switch (type) {
			case 2:
				Content imgContent = new Content("", type, activity, clubMember);
				conService.save(imgContent);

				// update activity's update date
				activity.setUpdateDate(imgContent.getSubmitDate());
				actService.update(activity);
				
				saveName = "images/content/img/" + imgContent.getId() + fileType;
				savePath = request.getServletContext().getRealPath("") + "/WEB-INF/classes/" + saveName;
				try {
					File file = new File(imageFile.getOriginalFilename());
					file.createNewFile();
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(imageFile.getBytes());
					fos.close();
					FileUtils.copyFile(file, new File(savePath));
					imgContent.setContent(saveName);
					conService.update(imgContent);
					
					result.put("contentId", imgContent.getId());
					result.put("content", imgContent.getContent());
					result.put("editorName", clubMember.getName());
					result.put("editorPhoto", clubMember.getUser().getPhoto());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					result.put("editingDate", sdf.format(imgContent.getSubmitDate()));
					
					result.put("type", 5);				// new image content created
					template.convertAndSend("topic/edit_activity/" + activity.getId(), result);
				} catch (IOException e) {
					e.printStackTrace();
					return result;
				}
				break;
			case 3:
				Content vidContent = new Content("", type, activity, clubMember);
				conService.save(vidContent);
				
				// update activity's update date
				activity.setUpdateDate(vidContent.getSubmitDate());
				actService.update(activity);
				
				saveName = "images/content/vid/" + vidContent.getId() + fileType;
				savePath = request.getServletContext().getRealPath("") + "/WEB-INF/classes/" + saveName;
				try {
					File file = new File(videoFile.getOriginalFilename());
					file.createNewFile();
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(videoFile.getBytes());
					fos.close();
					FileUtils.copyFile(file, new File(savePath));
					vidContent.setContent(saveName);
					conService.update(vidContent);
					
					result.put("contentId", vidContent.getId());
					result.put("content", vidContent.getContent());
					result.put("editorName", clubMember.getName());
					result.put("editorPhoto", clubMember.getUser().getPhoto());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					result.put("editingDate", sdf.format(vidContent.getSubmitDate()));
					
					result.put("type", 6);				// new text content created
					template.convertAndSend("topic/edit_activity/" + activity.getId(), result);
				} catch (IOException e) {
					e.printStackTrace();
					return result;
				}
				break;
			default:
				
		}
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping(value = "exit_editing", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> exitActivityEditing(String activityId,
										 		   String clubMemberId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Activity activity = actService.getActivityById(activityId);
		if (activity == null) {
			return result;
		}
		ClubMember clubMember = cmService.getClubMemberById(clubMemberId);
		if (clubMember == null) {
			return result;
		}
		
		Iterator<ClubMember> ite = activity.getEditors().iterator();
		while (ite.hasNext()) {
			// delete from the editor list
			if (ite.next().getId().equals(clubMember.getId())) {
				ite.remove();
				break;
			}
		}
		
		actService.update(activity);
		result.put("editorId", clubMember.getId());
		result.put("editorAmount", activity.getEditors().size());
		result.put("type", 2);				// an editor leaves
		template.convertAndSend("topic/edit_activity/" + activity.getId(), result);
		
		return result;
	}
	
	@RequestMapping(value = "publish", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> publishActivity(HttpSession session, 
											   String activityId,
											   String title) {
		Map<String, Object> result = new HashMap<String, Object>();
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (currentUser == null) {
			result.put("code", -1);		// need login
			return result;
		}
		Activity activity = actService.getActivityById(activityId);
		if (activity == null) {
			result.put("code", 0);		// wrong activityId
			return result;
		}
		ClubMember clubMember = cmService.getClubMemberByUserIdAndClubId(currentUser.getId(), activity.getClub().getId());
		if (!clubMember.getId().equals(activity.getClub().getManager().getId())) {
			result.put("code", -2);		// not the manager
			return result;
		}
		if (conService.getContentListByActivityId(activity.getId()).size() == 0) {
			result.put("code", -3);		// empty content is not allowed
			return result;
		}
		
		activity.setTitle(title);
		activity.setIsFinished(true);
		actService.update(activity);
		
		// send the shutdown command
		result.put("type", 7);				// close editing
		template.convertAndSend("topic/edit_activity/" + activity.getId(), result);
		
		// send subscription notification
		for (Subscription subscription : subService.getSubscriptionListByClubId(activity.getClub().getId())) {
			subscription.getUnreadActivities().add(activity);
			subService.update(subscription);
			result.put("unreadActivityCount", actService.getUnreadActivityListByUserId(subscription.getUser().getId()).size());
			template.convertAndSend("topic/subscription/" + subscription.getUser().getId(), result);
		}
		
		result.put("code", 1);
		return result;
	}
	
	@RequestMapping(value = "submit_comment", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> submitComment(HttpSession session, 
											 String activityId,
											 String content) {
		Map<String, Object> result = new HashMap<String, Object>();
		User currentUser = (User) session.getAttribute("USER_SESSION");
		if (currentUser == null) {
			result.put("code", -1);		// need login
			return result;
		}
		Activity activity = actService.getActivityById(activityId);
		if (activity == null) {
			result.put("code", 0);		// wrong activityId
			return result;
		}
		
		Comment comment = new Comment(currentUser, activity, content);
		cmtService.save(comment);
		
		result.put("code", 1);
		return result;
	}
	
}
