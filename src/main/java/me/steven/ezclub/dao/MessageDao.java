package me.steven.ezclub.dao;

import java.util.List;

import me.steven.ezclub.entity.Message;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class MessageDao extends GenDao<Message> {

	public void save(Message message) {
		super.save(message);
	}
	
	public void delete(Message message) {
		super.delete(message);
	}
	
	public void update(Message message) {
		Update update = new Update();
		update.set("isRead", message.getIsRead());		// only for setting read flag
		super.update(Query.query(Criteria.where("_id").is(message.getId())), update);
	}
	
	public Message getMessageById(String id) {
		return (Message) super.findOne(Query.query(Criteria.where("_id").is(id)));
	}
	
	public List<Message> getMessageListByAddresseeUserIdAndIsRead(String userId, boolean isRead) {
		return (List<Message>) super.findList(Query.query(Criteria.where("addressee.$id").is(new ObjectId(userId))
												.andOperator(Criteria.where("isRead").is(isRead)))
												.with(new Sort(Direction.DESC, "sentDate")));
	}
	
	public List<Message> getMessageListBySenderUserId(String userId) {
		return (List<Message>) super.findList(Query.query(Criteria.where("sender.$id").is(new ObjectId(userId)))
												.with(new Sort(Direction.DESC, "sentDate")));
	}
	
	@Override
	protected Class<Message> getEntityClass() {
		return Message.class;
	}

}
