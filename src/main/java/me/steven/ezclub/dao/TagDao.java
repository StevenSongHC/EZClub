package me.steven.ezclub.dao;

import me.steven.ezclub.entity.Tag;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class TagDao extends GenDao<Tag> {

	public void save(Tag tag) {
		super.save(tag);
	}
	
	public Tag getTagById(String id) {
		return (Tag) super.findOne(Query.query(Criteria.where("_id").is(id)));
	}
	
	public Tag getTagByName(String name) {
		return (Tag) super.findOne(Query.query(Criteria.where("name").is(name)));
	}
	
	@Override
	protected Class<Tag> getEntityClass() {
		return Tag.class;
	}

}
