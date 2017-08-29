package me.steven.ezclub.service;

import java.util.List;

import me.steven.ezclub.dao.MessageDao;
import me.steven.ezclub.entity.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	@Autowired
	private MessageDao msgDao;
	
	public void save(Message message) {
		msgDao.save(message);
	}
	
	public void delete(Message message) {
		msgDao.delete(message);
	}
	
	public void update(Message message) {
		msgDao.update(message);
	}
	
	public Message getMessageById(String id) {
		return msgDao.getMessageById(id);
	}
	
	public List<Message> getUnreadMessageListByAddresseeUserId(String userId) {
		return msgDao.getMessageListByAddresseeUserIdAndIsRead(userId, false);
	}
	
	public List<Message> getReadMessageListByAddresseeUserId(String userId) {
		return msgDao.getMessageListByAddresseeUserIdAndIsRead(userId, true);
	}
	
	public List<Message> getMessageListBySenderUserId(String userId) {
		return msgDao.getMessageListBySenderUserId(userId);
	}
	
}
