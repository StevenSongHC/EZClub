package me.steven.ezclub.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Club {

	@Id
	private String id;
	@Indexed(unique=true)
	private String cnName;
	private String enName;
	private String intro;
	private String badge;
	private Date createDate;
	private int status;				// 0=unchecked, 1=normal, -1=hide
	@DBRef
	private College college;
	@DBRef
	private ClubMember manager;
	@DBRef
	private List<Tag> tags;
	private Map<String, Object> data;
	
	public Club(String cnName, String enName, String intro, College college) {
		this.cnName = cnName;
		this.enName = enName;
		this.intro = intro;
		this.college = college;
		this.badge = "images/club-badge/default.png";
		this.createDate = new java.sql.Date(new java.util.Date().getTime());
		this.status = 0;
		
		this.tags = new ArrayList<Tag>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getBadge() {
		return badge;
	}
	public void setBadge(String badge) {
		this.badge = badge;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public College getCollege() {
		return college;
	}
	public void setCollege(College college) {
		this.college = college;
	}
	public ClubMember getManager() {
		return manager;
	}
	public void setManager(ClubMember manager) {
		this.manager = manager;
	}
	public List<Tag> getTags() {
		return tags;
	}
	public void setTags(List<Tag> tags) {
		this.tags = tags;
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
