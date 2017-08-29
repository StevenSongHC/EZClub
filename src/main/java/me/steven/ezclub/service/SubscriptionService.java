package me.steven.ezclub.service;

import java.util.List;

import me.steven.ezclub.dao.SubscriptionDao;
import me.steven.ezclub.entity.Subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {
	
	@Autowired
	private SubscriptionDao subDao;
	
	public void save(Subscription subscription) {
		subDao.save(subscription);
	}
	
	public void delete(Subscription subscription) {
		subDao.delete(subscription);
	}
	
	public void update(Subscription subscription) {
		subDao.update(subscription);
	}
	
	public Subscription getSubscriptionByUserIdAndClubId(String userId, String clubId) {
		return subDao.getSubscriptionByUserIdAndClubId(userId, clubId);
	}
	
	public List<Subscription> getSubscriptionListByUserId(String userId) {
		return subDao.getSubscriptionListByUserId(userId);
	}

	public List<Subscription> getSubscriptionListByClubId(String clubId) {
		return subDao.getSubscriptionListByClubId(clubId);
	}

}
