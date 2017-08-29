package me.steven.ezclub.service;

import java.util.List;

import me.steven.ezclub.dao.GameDao;
import me.steven.ezclub.entity.Game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
	
	@Autowired
	GameDao gDao;
	
	public void save(Game game) {
		gDao.save(game);
	}
	
	public void update(Game game) {
		gDao.update(game);
	}
	
	public List<Game> getGameList() {
		return gDao.getGameList();
	}
	
	/*public Pagination<Game> getGameListPage(int currentPage, int pageSize, String baseUrl) {
		return gDao.getGameListPage(currentPage, pageSize, baseUrl);
	}*/
	
	public Game getGameById(String id) {
		return gDao.getGameById(id);
	}
	
	public Game getGameByName(String name) {
		return gDao.getGameByName(name);
	}
	
	/*public Pagination<Game> searchGameListPage(String key, String keyword, int currentPage, int pageSize, String baseUrl) {
		return gDao.searchGameListPage(key, keyword, currentPage, pageSize, baseUrl);
	}*/
	
	public void deleteById(String id) {
		gDao.deleteById(id);
	}
	
}
