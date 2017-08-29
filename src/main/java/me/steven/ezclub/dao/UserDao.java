package me.steven.ezclub.dao;

import java.util.List;
import me.steven.ezclub.entity.User;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends GenDao<User> {

	public void save(User user) {
		super.save(user);
	}
	
	public void delete(User user) {
		super.delete(user);
	}
	
	public User getUserById(String id) {
		return (User) super.findOne(Query.query(Criteria.where("_id").is(id)));
	}
	
	public User getUserByEmail(String email) {
		return (User) super.findOne(Query.query(Criteria.where("email").is(email)));
	}
	
	public User getUserByNickname(String nickname) {
		return (User) super.findOne(Query.query(Criteria.where("nickname").is(nickname)));
	}
	
	public void update(User user) {
		Update update = new Update();
		update.set("email", user.getEmail());
		update.set("nickname", user.getNickname());
		update.set("password", user.getPassword());
		update.set("photo", user.getPhoto());
		update.set("sex", user.getSex());
		update.set("lastLoginDate", user.getLastLoginDate());
		update.set("birthDate", user.getBirthDate());
		update.set("status", user.getStatus());
		update.set("interests", user.getInterests());
		update.set("province", user.getProvince());
		update.set("city", user.getCity());
		update.set("college", user.getCollege());
		super.update(Query.query(Criteria.where("_id").is(user.getId())), update);
	}
	
	public List<User> getUserList() {
		return (List<User>) super.findList(new Query().with(new Sort(Direction.DESC, "_id")));
	}
	
	/*public Pagination<User> getUserListPage(int currentPage, int pageSize, String baseUrl) {
		return super.queryPage(new Query().with(new Sort(Direction.DESC, "_id")), (currentPage - 1) * pageSize, pageSize, baseUrl);
	}*/
	
	public List<User> getUserListByGameId(String gid) {
		return (List<User>) super.findList(Query.query(Criteria.where("games._id").is(new ObjectId(gid))).with(new Sort(Direction.DESC, "_id")));
	}
	
	public User getUserByName(String name) {
		return (User) super.findOne(Query.query(Criteria.where("name").is(name)));
	}
	
	/*public Pagination<User> searchUserListPage(String key, String keyword, int currentPage, int pageSize, String baseUrl) {
		return super.queryPage(Query.query(Criteria.where(key).regex(Pattern.compile(keyword, Pattern.CASE_INSENSITIVE))), (currentPage - 1) * pageSize, pageSize, baseUrl);
	}*/
	
	public Long countUserByGameId(String gid) {
		return super.getPageCount(Query.query(Criteria.where("games._id").is(new ObjectId(gid))));
	}
	
	@Override
	protected Class<User> getEntityClass() {
		return User.class;
	}
	
}
