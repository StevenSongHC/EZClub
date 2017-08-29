package me.steven.ezclub.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Message {

	@Id
	private String id;
	private String content;
	private boolean isRead;
	private Date sentDate;
	@DBRef
	private User sender;
	@DBRef
	private User addressee;
	
	public Message(User sender, User addressee, String content) {
		if (addressee == null) {
			System.out.println("Null addressee is not allowed!!!");
			return;
		}
		this.sender = sender;
		this.addressee = addressee;
		this.content = content;
		
		this.isRead = false;
		this.sentDate = new java.sql.Date(new java.util.Date().getTime());
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
	public boolean getIsRead() {
		return isRead;
	}
	public void setIsRead(boolean isRead) {
		this.isRead = isRead;
	}
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public User getAddressee() {
		return addressee;
	}
	public void setAddressee(User addressee) {
		this.addressee = addressee;
	}
	
}
