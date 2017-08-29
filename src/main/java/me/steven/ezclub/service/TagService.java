package me.steven.ezclub.service;

import me.steven.ezclub.dao.TagDao;
import me.steven.ezclub.entity.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

	@Autowired
	private TagDao tagDao;
	
	public void save(Tag tag) {
		tagDao.save(tag);
	}
	
	public Tag getTagById(String id) {
		return tagDao.getTagById(id);
	}
	
	public Tag getTagByName(String name) {
		return tagDao.getTagByName(name);
	}
	
}
