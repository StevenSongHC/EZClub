package me.steven.ezclub.entity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "groups")
public class Group {

	@Id
	private String id;
	private int year;						// year=0 -> default group
	private String wholePhoto;
	private String newbiePhoto;
	@DBRef
	private Club club;
	private Map<String, Object> data;
	
	public Group(Club club, int year) {
		this.club = club;
		this.year = year;
		wholePhoto = "images/club-group/whole/default.png";
		newbiePhoto = "images/club-group/newbie/default.png";
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getWholePhoto() {
		return wholePhoto;
	}
	public void setWholePhoto(String wholePhoto) {
		this.wholePhoto = wholePhoto;
	}
	public String getNewbiePhoto() {
		return newbiePhoto;
	}
	public void setNewbiePhoto(String newbiePhoto) {
		this.newbiePhoto = newbiePhoto;
	}
	public Club getClub() {
		return club;
	}
	public void setClub(Club club) {
		this.club = club;
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
