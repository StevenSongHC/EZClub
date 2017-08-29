package me.steven.ezclub.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clubMember")
public class ClubMember {

	@Id
	private String id;
	private String name;
	private String contact;
	private Date joinDate;
	@DBRef
	private User user;
	@DBRef
	private Club club;
	@DBRef
	private Department department;
	@DBRef
	private Group group;
	
	public ClubMember(String name, User user, Club club, Group group) {
		this.name = name;
		this.user = user;
		this.club = club;
		this.group = group;
		this.contact = "";
		this.joinDate = new java.sql.Date(new java.util.Date().getTime());
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
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
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	
}
