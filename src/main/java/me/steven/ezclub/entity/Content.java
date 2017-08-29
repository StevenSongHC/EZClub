package me.steven.ezclub.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Content {

	@Id
	private String id;
	private int type;				// 1=Text, 2=Image, 3=Video
	private String content;
	private Date submitDate;
	private int order;
	@DBRef
	private Activity activity;
	@DBRef
	private ClubMember creator;
	
	public Content(String content, int type, Activity activity, ClubMember creator) {
		this.content = content;
		this.type = type;
		this.creator = creator;
		this.activity = activity;
		this.submitDate = new java.sql.Date(new java.util.Date().getTime());
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public ClubMember getCreator() {
		return creator;
	}
	public void setCreator(ClubMember creator) {
		this.creator = creator;
	}
	
}
