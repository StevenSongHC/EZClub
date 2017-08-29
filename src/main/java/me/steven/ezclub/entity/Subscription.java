package me.steven.ezclub.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Subscription {

	@Id
	private String id;
	private Date subscribeDate;
	@DBRef
	private User user;
	@DBRef
	private Club club;
	@DBRef
	private List<Activity> unreadActivities;
	
	public Subscription(User user, Club club) {
		this.user = user;
		this.club = club;
		
		this.subscribeDate = new java.sql.Date(new java.util.Date().getTime());
		this.unreadActivities = new ArrayList<Activity>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getSubscribeDate() {
		return subscribeDate;
	}
	public void setSubscribeDate(Date subscribeDate) {
		this.subscribeDate = subscribeDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Club getClub() {
		return club;
	}
	public void setClub(Club club) {
		this.club = club;
	}
	public List<Activity> getUnreadActivities() {
		return unreadActivities;
	}
	public void setUnreadActivities(List<Activity> unreadActivities) {
		this.unreadActivities = unreadActivities;
	}
	
}
