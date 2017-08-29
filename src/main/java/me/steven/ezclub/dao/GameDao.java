package me.steven.ezclub.dao;

import java.util.List;
import java.util.regex.Pattern;

import me.steven.ezclub.entity.Game;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao extends GenDao<Game> {
	
	public void save(Game game) {
		super.save(game);
	}
	
	public void update(Game game) {
		Update update = new Update();
		update.set("name", game.getName());
		update.set("price", game.getPrice());
		super.update(Query.query(Criteria.where("_id").is(game.getId())), update);
	}

	public List<Game> getGameList() {
		return (List<Game>) super.findList(new Query().with(new Sort(Direction.DESC, "_id")));
	}
	
	/*public Pagination<Game> getGameListPage(int currentPage, int pageSize, String baseUrl) {
		return super.queryPage(new Query().with(new Sort(Direction.DESC, "_id")), (currentPage - 1) * pageSize, pageSize, baseUrl);
	}*/
	
	public Game getGameById(String id) {
		return (Game) super.findOne(Query.query(Criteria.where("_id").is(id)));
	}
	
	public Game getGameByName(String name) {
		return (Game) super.findOne(Query.query(Criteria.where("name").is(name)));
	}
	
	/*public Pagination<Game> searchGameListPage(String key, String keyword, int currentPage, int pageSize, String baseUrl) {
		return super.queryPage(Query.query(Criteria.where(key).regex(Pattern.compile(keyword, Pattern.CASE_INSENSITIVE))), (currentPage - 1) * pageSize, pageSize, baseUrl);
	}*/
	
	public void deleteById(String id) {
		super.delete(Query.query(Criteria.where("_id").is(id)));
	}
	
	@Override
	protected Class<Game> getEntityClass() {
		return Game.class;
	}

}
