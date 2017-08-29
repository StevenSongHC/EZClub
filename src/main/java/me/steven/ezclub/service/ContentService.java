package me.steven.ezclub.service;

import java.util.List;

import me.steven.ezclub.dao.ContentDao;
import me.steven.ezclub.entity.Content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {
	
	@Autowired
	private ContentDao conDao;
	
	public void save(Content content) {
		conDao.save(content);
	}
	
	public void delete(Content content) {
		conDao.delete(content);
	}
	
	public void update(Content content) {
		conDao.update(content);
	}
	
	public Content getContentById(String id) {
		return conDao.getContentById(id);
	}
	
	public List<Content> getContentListByActivityId(String activityId) {
		return conDao.getContentListByActivityId(activityId);
	}

	public List<Content> getContentListByClubMemberIdAndActivityId(String clubMemberId, String activityId) {
		return conDao.getContentListByClubMemberIdAndActivityId(clubMemberId, activityId);
	}
	
	public List<Content> getContentListByActivityIdAndType(String activityId, int type) {
		return conDao.getContentListByActivityIdAndType(activityId, type);
	}
	
}
