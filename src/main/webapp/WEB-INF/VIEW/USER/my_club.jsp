<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
String basepath = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../include/include_style.jsp" flush="true"/>
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/user-mystuff.css">
<title>我的社团 | EZ Club</title>
</head>
<body>
<div id="main">
	<input type="hidden" id="current-clubid" value="${loadedMember.club.id}">
	<div class="row">
		<div class="col-md-2 left-wrapper">
			<ul id="club-list">
				<c:forEach items="${myClubMemberList}" var="member">
					<li class="overflow-ellipsis <c:if test="${member.club.id == club.id}"> active</c:if>">
						<a title="${member.club.cnName}" href="<%=basepath%>/user/club/${member.club.id}">${member.club.cnName}</a>
					</li>
				</c:forEach>
			</ul>
			<div id="create-club">
				<a href="<%=basepath%>/create_club" target="_blank">添加社团</a>
			</div>
		</div>
		<div class="col-md-5 middle-wrapper">
			<c:if test="${club.manager.user.id == sessionScope.USER_SESSION.id}">
				<button class="btn btn-default btn-lg" id="add-activity">新活动</button>
			</c:if>
			<div id="new-activity">
				<input type="text" id="new-activity-title" class="form-control" placeholder="活动名称">
				<button class="btn btn-default" onclick="javascript:addActivity('${club.id}')">确认</button>
			</div>
			<c:forEach items="${activityList}" var="activity">
				<div class="activity-item">
					<div class="update-date"><fmt:formatDate pattern='yyyy-MM-dd HH:mm:ss' value='${activity.updateDate}'/></div>
					<c:choose>
						<c:when test="${activity.isFinished}">
							<a href="<%=basepath%>/activity/${activity.id}" class="title" target="_blank">${activity.title}</a>
						</c:when>
						<c:otherwise>
							<span class="label label-default">未发布</span>
							<a href="<%=basepath%>/activity/edit/${activity.id}" class="title" target="_blank">${activity.title}</a>
						</c:otherwise>
					</c:choose>
					<div class="content-statistics"><u>${activity.data.textContentAmount}</u>段文字</div>
					<div class="content-statistics"><u>${activity.data.imageContentAmount}</u>张图片</div>
					<div class="content-statistics"><u>${activity.data.videoContentAmount}</u>个视频</div>
				</div>
			</c:forEach>
		</div>
		<div class="col-md-5 right-wrapper">
			<c:choose>
				<c:when test="${club != null}">
					<h3>
						<img src="<%=basepath%>/${myClubMember.club.badge}">
						<b>${myClubMember.club.cnName}</b>&nbsp;<c:if test="${myClubMember.group.year != 0}"><small>(${myClubMember.group.year})</small></c:if>
						<c:if test="${club.manager.user.id == sessionScope.USER_SESSION.id}">
							<a href="<%=basepath%>/club/manage/${myClubMember.club.id}" class="btn btn-default" id="manage-club" target="_blank">管理入口</a>
						</c:if>
					</h3>
					<div class="group-photo">
						<div class="tab-content">
							<div class="tab-pane fade in active" id="whole-photo">
								<img src="<%=basepath%>//${myClubMember.group.wholePhoto}"/>
							</div>
							<div class="tab-pane fade" id="newbie-photo">
								<img src="<%=basepath%>//${myClubMember.group.newbiePhoto}"/>
							</div>
						</div>
						<ul class="nav nav-tabs">
							<li class="active"><a href="#whole-photo" data-toggle="tab" data-type="whole">全体合照</a></li>
							<li><a href="#newbie-photo" data-toggle="tab" data-type="newbie">新生合照</a></li>
						</ul>
					</div>
					<div class="my-group-info">
						<input type="text" class="form-control name" placeholder="我在社团中的昵称（必填）" value="${myClubMember.name}">
						<input type="text" class="form-control contact" placeholder="联系方式" value="${myClubMember.contact}">
						<button class="btn btn-default" onclick="javascript:updateMemberInfo('${myClubMember.id}')">保存</button>
					</div>
					<div class="group-members">
						<c:forEach items="${departmentList}" var="department">
							<div class="item">
								<div class="header">${department.title}</div>
								<ul>
									<c:forEach items="${groupClubMemberList}" var="member">
										<c:if test="${member.department.id == department.id}">
											<li data-cmid="${member.id}"<c:if test="${member.user.id == sessionScope.USER_SESSION.id}"> class="self"</c:if>><span class="member-name" title="${member.user.nickname}">${member.name}</span><span class="member-contact" title="联系方式">${member.contact}</span></li>
										</c:if>
									</c:forEach>
								</ul>
							</div>
						</c:forEach>
						<div class="default">
							<div class="header">未分组</div>
							<ul>
								<c:forEach items="${defaultMembers}" var="member">
									<li data-cmid="${member.id}"<c:if test="${member.user.id == sessionScope.USER_SESSION.id}"> class="self"</c:if>><span class="member-name" title="${member.user.nickname}">${member.name}</span><span class="member-contact" title="联系方式">${member.contact}</span></li>
								</c:forEach>
							</ul>
						</div>
					</div>
					<c:if test="${club.manager.user.id != sessionScope.USER_SESSION.id}">
						<input type="password" id="password-validate" class="form-control" data-display=0 placeholder="输入登陆密码以继续">
						<br>
						<button class="btn btn-warning" onclick="javascript:quitClub('${club.id}')">退出社团</button>
					</c:if>
				</c:when>
				<c:otherwise>
					推荐列表
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>

<jsp:include page="../include/include_navbar.jsp" flush="true"/>
<script type="text/javascript">
(function() {
	$("#my-club").addClass("active");
	
	$("#left-wrapper").mCustomScrollbar({theme: "minimal-dark"});
	$("#middle-wrapper").mCustomScrollbar({theme: "minimal-dark"});
	$("#right-wrapper").mCustomScrollbar({theme: "minimal-dark"});
	
	$("#add-activity").click(function(e) {
		$(this).hide();
		$("#new-activity").show();
		$("#new-activity-title").focus();
	});
})();

function loadClub(id) {
	if (id == $("#current-clubid").val()) {
		return;
	}
	else {
		$("#current-clubid").val(id);
	}
}

function updateMemberInfo(id) {
	var name = $(".my-group-info .name").val().trim();
	var contact = $(".my-group-info .contact").val().trim();
	if (name.length == 0) {
		alert("请填入必要的昵称");
		return;
	}
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/user/update_member_info",
		dataType: "JSON",
		data: {
			cmid: id,
			name: name,
			contact: contact
		}
	}).done(function(data) {
		switch (data.code) {
			case 0:
				alert("信息出错");
				break;
			case 1:
				$(".group-members li[data-cmid='" + id + "']").html(name + "（" + contact + "）");
				alert("保存成功");
				break;
			default:
				alert("保存失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}

function quitClub(clubId) {
	var pv = $("#password-validate");
	if ($(pv).attr("data-display") != 1) {
		$(pv).attr("data-display", 1).show().focus();
		return;
	}
	var password = $(pv).val();
	if (password.length == 0) {
		alert("请输入密码以继续");
		$(pv).focus();
		return;
	}
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/user/quit_club",
		dataType: "JSON",
		data: {
			clubId: clubId,
			password: password
		}
	}).done(function(data) {
		switch (data.code) {
			case -2:
				alert("不是该社团成员");
				window.location.reload();
				break;
			case -1:
				alert("密码错误");
				break;
			case 0:
				alert("社团数据出错");
				break;
			case 1:
				alert("成功退出社团");
				window.location.href = "<%=basepath%>/user/club";
				break;
			default:
				alert("操作失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}

function addActivity(clubId) {
	var title = $("#new-activity-title").val().trim();
	if (title.length == 0) {
		// close
		$("#new-activity").hide();
		$("#add-activity").show();
		return;
	}
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/club/add_activity",
		dataType: "JSON",
		data: {
			clubId: clubId,
			title: title
		}
	}).done(function(data) {
		switch (data.code) {
			case -2:
				alert("本社团已发过同样名称的活动，请做适当修改");
				$("#new-activity-title").focus();
				break;
			case -1:
				alert("没有新建活动的权限");
				break;
			case 0:
				alert("社团数据出错");
				break;
			case 1:
				var newActivity = "<div class='activity-item'>";
				newActivity += "<div class='update-date'>" + data.newActivityUpdateDate + "</div>";
				newActivity += "<span class='label label-default'>未发布</span>";
				newActivity += "<a href='<%=basepath%>/activity/edit/" + data.newActivityId + "' class='title' target='_blank'>" + data.newActivityTitle + "</a>";
				newActivity += "<div class='content-statistics'><u>0</u>段文字</div>";
				newActivity += "<div class='content-statistics'><u>0</u>张图片</div>";
				newActivity += "<div class='content-statistics'><u>0</u>个视频</div>";
				newActivity += "</div>";
				$("#new-activity").after(newActivity);
				$("#new-activity").hide();
				$("#add-activity").show();
				$("#new-activity-title").val("");
				break;
			default:
				alert("新建失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
</script>
</body>
</html>