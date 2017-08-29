package me.steven.ezclub.service;

import java.util.Iterator;
import java.util.List;

import me.steven.ezclub.dao.ClubMemberDao;
import me.steven.ezclub.entity.ClubMember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClubMemberService {

	@Autowired
	private ClubMemberDao cmDao;
	
	public void save(ClubMember clubMember) {
		cmDao.save(clubMember);
	}
	
	public void delete(ClubMember clubMember) {
		cmDao.delete(clubMember);
	}
	
	public ClubMember getClubMemberById(String id) {
		return cmDao.getClubMemberById(id);
	}
	
	public ClubMember getClubMemberByUserIdAndClubId(String userId, String clubId) {
		return cmDao.getClubMemberByUserIdAndClubId(userId, clubId);
	}
	
	public List<ClubMember> getClubMemberListByClubId(String clubId) {
		return cmDao.getClubMemberListByClubId(clubId);
	}
	
	public List<ClubMember> getAvailableClubMemberListByUserId(String userId) {
		List<ClubMember> clubMemberList = cmDao.getClubMemberListByUserId(userId);
		// remove no-pass and null data
		if (clubMemberList.size() > 0) {
			Iterator<ClubMember> it = clubMemberList.iterator();
			while (it.hasNext()) {
				ClubMember clubMember = it.next();
				if (clubMember.getClub() == null || clubMember.getClub().getStatus() != 1) {
					it.remove();
				}
			}
		}
		return clubMemberList;
	}
	
	public List<ClubMember> getClubMemberListByGroupId(String groupId) {
		return cmDao.getClubMemberListByGroupId(groupId);
	}
	
	public List<ClubMember> getClubMemberListByGroupIdAndDepartmentId(String groupId, String departmentId) {
		return cmDao.getClubMemberListByGroupIdAndDepartmentId(groupId, departmentId);
	}
	
	public void update(ClubMember clubMember) {
		cmDao.update(clubMember);
	}
	
}
