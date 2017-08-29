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
public class User {

	@Id
	private String id;
	@Indexed(unique=true)
	private String email;
	@Indexed(unique=true)
	private String nickname;
	private String password;
	private String photo;
	private int sex;					// 0=secret, 1=male, 2=female
	private Date joinDate;
	private Date lastLoginDate;
	private Date birthDate;
	private int status;					// 1=normal, -1=restricted
	private int isAdmin;				// 2779=admin
	@DBRef
	private List<Interest> interests;
	@DBRef
	private Province province;
	@DBRef
	private City city;
	@DBRef
	private College college;
	private Map<String, Object> data;	// extra data, something like statistics
	
	// set default
	public User() {
		this.photo = "images/portrait/default.png";
		Date currentDate = new java.sql.Date(new java.util.Date().getTime());
		this.joinDate = currentDate;
		this.lastLoginDate = currentDate;
		this.status = 1;
		this.isAdmin = 0;
		
		interests = new ArrayList<Interest>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public java.util.Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(java.util.Date joinDate) {
		this.joinDate = joinDate;
	}
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<Interest> getInterests() {
		return interests;
	}
	public void setInterests(List<Interest> interests) {
		this.interests = interests;
	}
	public void addInterest(Interest interest) {
		this.interests.add(interest);
	}
	public int getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}
	public Province getProvince() {
		return province;
	}
	public void setProvince(Province province) {
		this.province = province;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public College getCollege() {
		return college;
	}
	public void setCollege(College college) {
		this.college = college;
	}
	public Map<String, Object> getData() {
		// create if null
		if (data == null) {
			data = new HashMap<String, Object>();
		}
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
}