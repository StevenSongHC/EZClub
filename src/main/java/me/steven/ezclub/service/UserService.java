package me.steven.ezclub.service;

import java.util.List;

import me.steven.ezclub.dao.UserDao;
import me.steven.ezclub.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserDao uDao;
	
	public void save(User user) {
		uDao.save(user);
	}
	
	public void delete(User user) {
		uDao.delete(user);
	}
	
	public User getUserById(String id) {
		return uDao.getUserById(id);
	}
	
	public User getUserByEmail(String email) {
		return uDao.getUserByEmail(email);
	}
	
	public User getUserByNickname(String nickname) {
		return uDao.getUserByNickname(nickname);
	}
	
	public void update(User user) {
		uDao.update(user);
	}
	
	public List<User> getUserList() {
		return uDao.getUserList();
	}
	
	/*public Pagination<User> getUserListPage(int currentPage, int pageSize, String baseUrl) {
		return uDao.getUserListPage(currentPage, pageSize, baseUrl);
	}
	
	public Pagination<User> searchUserListPage(String key, String keyword, int currentPage, int pageSize, String baseUrl) {
		return uDao.searchUserListPage(key, keyword, currentPage, pageSize, baseUrl);
	}*/
	
	public List<User> getUserListByGameId(String gid) {
		return uDao.getUserListByGameId(gid);
	}
	
	public Long countUserByGameId(String gid) {
		return uDao.countUserByGameId(gid);
	}
	
}
