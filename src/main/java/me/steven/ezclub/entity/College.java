package me.steven.ezclub.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class College {

	@Id
	private String id;
	@Indexed(unique=true)
	private String cnName;
	private String enName;
	private String shortName;
	private String intro;
	private String badge;
	private String photo;
	private Date createDate;
	/*private String creatorIPAddress;*/
	private int status;			// 0=unchecked, 1=normal, -1=hide
	@DBRef
	private City city;
	private Map<String, Object> data;
	
	public College(String cnName, String enName, String shortName, String intro, City city) {
		this.cnName = cnName;
		this.enName = enName;
		this.shortName = shortName;
		this.intro = intro;
		this.city = city;
		this.badge = "images/college-badge/default.png";
		this.photo = "images/college-photo/default.png";
		this.createDate = new java.sql.Date(new java.util.Date().getTime());
		this.status = 0;
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
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
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
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/*public String getCreatorIPAddress() {
		return creatorIPAddress;
	}
	public void setCreatorIPAddress(String creatorIPAddress) {
		this.creatorIPAddress = creatorIPAddress;
	}*/
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
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
