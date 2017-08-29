package me.steven.ezclub.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Comment {

	@Id
	private String id;
	private String content;
	private Date submitDate;
	@DBRef
	private User user;
	@DBRef
	private Activity activity;
	
	public Comment(User user, Activity activity, String content) {
		this.user = user;
		this.activity = activity;
		this.content = content;
		
		this.submitDate = new java.sql.Date(new java.util.Date().getTime());
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
}
