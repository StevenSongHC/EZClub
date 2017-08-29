package me.steven.ezclub.service;

import java.util.ArrayList;
import java.util.List;

import me.steven.ezclub.dao.ActivityDao;
import me.steven.ezclub.dao.SubscriptionDao;
import me.steven.ezclub.entity.Activity;
import me.steven.ezclub.entity.Subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
	
	@Autowired
	private ActivityDao actDao;
	@Autowired
	private SubscriptionDao subDao;
	
	public void save(Activity activity) {
		actDao.save(activity);
	}
	
	public void delete(Activity activity) {
		actDao.delete(activity);
	}
	
	public void update(Activity activity) {
		actDao.update(activity);
	}
	
	public Activity getActivityById(String id) {
		return actDao.getActivityById(id);
	}
	
	public Activity getActivityByClubIdAndTitle(String clubId, String title) {
		return actDao.getActivityByClubIdAndTitle(clubId, title);
	}
	
	public List<Activity> getAllActivityListByClubId(String clubId) {
		return actDao.getAllActivityListByClubId(clubId);
	}
	
	public List<Activity> getFinishedActivityListByClubId(String clubId) {
		return actDao.getFinishedActivityListByClubId(clubId);
	}
	
	public List<Activity> getUnreadActivityListByUserId(String userId) {
		List<Activity> unreadList = new ArrayList<Activity>();
		for (Subscription subscription : subDao.getSubscriptionListByUserId(userId)) {
			unreadList.addAll(subscription.getUnreadActivities());
		}
		return unreadList;
	}

}
