package me.steven.ezclub.util;

import java.text.SimpleDateFormat;

import me.steven.ezclub.entity.Club;
import me.steven.ezclub.entity.ClubMember;

public class MessageUtil {
	
	public static String newClubCheckedNotify(Club newClub, String extraInfo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String content = "你于" + sdf.format(newClub.getCreateDate()) + "新建的<b>社团</b>【" + newClub.getCnName() + "】";
		if (newClub.getStatus() == 1) {
			content += "已通过管理员审核。<br><a href='../club/manage/" + newClub.getId() + "' target='_blank'>点此进行管理</a>";
		}
		else {
			content += "未能通过管理员审核。";
			if (extraInfo != null && !extraInfo.equals("")) {
				content += "（" + extraInfo + "）";
			}
		}
		return content;
	}
	
	public static String clubMemberAddedNotify(ClubMember clubMember) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String content = clubMember.getClub().getManager().getUser().getNickname() + "已于" + sdf.format(clubMember.getJoinDate()) + 
							"将你加入了【<a href='../user/club/" + clubMember.getClub().getId() +  "' target='_blank'>" + clubMember.getClub().getCnName() + "</a>】社团。";
		return content;
	}
	
	public static String becomeManagerNotify(ClubMember oldManager) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String content = oldManager.getUser().getNickname() + "已于" + sdf.format(new java.sql.Date(new java.util.Date().getTime())) + 
							"将【" + oldManager.getClub().getCnName() + "】社团的管理员权限移交给你。" +
							"<br><a href='../club/manage/" + oldManager.getClub().getId() + "' target='_blank'>点此进行管理</a>";
		return content;
	}
	
}
