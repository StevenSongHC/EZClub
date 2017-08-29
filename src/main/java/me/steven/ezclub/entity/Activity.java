package me.steven.ezclub.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Activity {

	@Id
	private String id;
	private String title;
	private Date updateDate;
	private boolean isFinished;
	@DBRef
	private Club club;
	@DBRef
	private List<ClubMember> editors;
	private Map<String, Object> data;
	
	public Activity(String title, Club club) {
		this.title = title;
		this.club = club;
		updateDate = new java.sql.Date(new java.util.Date().getTime());
		this.isFinished = false;
		
		editors = new ArrayList<ClubMember>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public boolean getIsFinished() {
		return isFinished;
	}
	public void setIsFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	public Club getClub() {
		return club;
	}
	public void setClub(Club club) {
		this.club = club;
	}
	public List<ClubMember> getEditors() {
		return editors;
	}
	public void setEditors(List<ClubMember> editors) {
		this.editors = editors;
	}
	public Map<String, Object> getData() {
		if (data == null) {
			data = new HashMap<String, Object>();
		}
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
}
