<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
String basepath = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../include/include_style.jsp" flush="true"/>
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/manage-club.css">
<title>社团【${club.cnName}】管理 | EZ Club</title>
</head>
<body>
<div id="main">
	<input type="hidden" id="clubid" value="${club.id}">
	<div class="row">
		<div class="col-md-5 left-wrapper">
			<div class="club-name">
				<span class="cn-name sb">${club.cnName}</span>
				<span class="en-name sb" <c:if test="${club.enName == ''}">style="display: none;"</c:if>>${club.enName}</span>
				<input type="text" class="form-control hb" id="cn-name" value="${club.cnName}" placeholder="社团名字（必填）" maxlength="12">
				<input type="text" class="form-control hb" id="en-name" value="${club.enName}" placeholder="社团高大上的英文名">
				<button class="btn btn-default btn-sm sb" id="change-name" onclick="javascript:changeName()">更名</button>
				<button class="btn btn-success btn-sm hb" onclick="javascript:doChangeName()">确认</button>
				<button class="btn btn-danger btn-sm hb" onclick="javascript:cancelChangeName()">取消</button>
			</div>
			<div class="club-badge">
				<img src="<%=basepath%>/${club.badge}">
				<form action="<%=basepath%>/club/manage/upload_badge" id="badge-form" method="post" enctype="multipart/form-data">
					<input type="file" name="badgeFile" id="badge">
					<input type="submit" value="上传团徽" class="btn btn-default">
					<input type="hidden" name="clubId" value="${club.id}">
					<input type="hidden" name="fileType" value=".png">
				</form>
				<div class="progress">
					<div id="badge-progress-bar" class="progress-bar" role="progressbar" style="width: 0%">
						<span></span>
					</div>
				</div>
			</div>
			<div class="position">
				<span>${club.college.city.province.cnName}</span>
				<span>${club.college.city.cnName}</span>
				<span>${club.college.cnName}</span>
			</div>
			<div class="statistics">
				<span>成员数：<b>${memberCount}</b></span>
				<span>活动数：<b>${activityCount}</b></span>
				<span>关注数：<b>${subscriptionCount}</b></span>
			</div>
			<div class="club-intro">
				<div class="wrapper">
					<div class="intro-header">社团简介</div>
					<textarea class="form-control" rows="7" id="intro" disabled="disabled">${club.intro}</textarea>
				</div>
				<button class="btn btn-default sb" id="edit-intro" onclick="javascript:editIntro()">编辑</button>
				<button class="btn btn-primary hb" onclick="javascript:updateIntro()">保存</button>
			</div>
			<div class="club-tags">
				<p class="lead">标签</p>
				<div id="tags">
					<c:forEach items="${club.tags}" var="tag">
						<div class="item" data-tagid="${tag.id}">
							<span class="name">${tag.name}</span>
							<span class="glyphicon glyphicon-remove ope" onclick="javascript:removeTag('${tag.id}')"></span>
						</div>
					</c:forEach>
					<span class="add-tag glyphicon glyphicon-plus" onclick="javascript:addTag()"></span>
				</div>
			</div>
		</div>
		<div class="col-md-6 col-md-offset-1 right-wrapper">
			<div class="member-assignment">
				<input type="text" class="form-control" id="member-nickname" placeholder="成员昵称">
				<select id="member-year">
				</select>
				<select id="member-department">
					<option value="0">未分组</option>
					<c:forEach items="${departmentList}" var="department">
						<option value="${department.id}">${department.title}</option>
					</c:forEach>
				</select>
				<button class="btn btn-info" onclick="javascript:addMember()">添加（移动成员）</button>
			</div>
			<div class="member-manage">
				<div class="groups"<c:if test="${empty groupList}">style="display: none;"</c:if>>
					<div id="group-year">
						<c:forEach items="${groupList}" var="group" varStatus="status">
							<c:choose>
								<c:when test="${status.count == 1}">
									<span class="active" data-year=${group.year}>${group.year}</span>
								</c:when>
								<c:otherwise>
									<span class="item" data-year=${group.year}>${group.year}</span>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<div style="clear: both;"></div>
					</div>
					<div id="group-photo">
						<div class="tab-content">
							<div class="tab-pane fade in active" id="whole-photo">
								<img src="<%=basepath%>/${loadedGroup.wholePhoto}"/>
							</div>
							<div class="tab-pane fade" id="newbie-photo">
								<img src="<%=basepath%>/${loadedGroup.newbiePhoto}"/>
							</div>
						</div>
						<ul class="nav nav-tabs">
							<li class="active"><a href="#whole-photo" data-toggle="tab" data-type="whole">全体合照</a></li>
							<li><a href="#newbie-photo" data-toggle="tab" data-type="newbie">新生合照</a></li>
						</ul>
						<form action="<%=basepath%>/club/manage/upload_photo" id="photo-form" method="post" enctype="multipart/form-data">
							<input type="file" name="photoFile" id="photo">
							<input type="submit" value="上传全体合照" class="btn btn-default btn-success">
							<input type="hidden" name="groupId" value="${loadedGroup.id}">
							<input type="hidden" name="type" value="whole">
							<input type="hidden" name="fileType" value=".png">
						</form>
						<div class="progress">
							<div id="photo-progress-bar" class="progress-bar" role="progressbar" style="width: 0%">
								<span></span>
							</div>
						</div>
					</div>
				</div>
				<div class="departments" data-gpid="${loadedGroup.id}">
					<c:forEach items="${departmentList}" var="department">
						<div class="item" data-deptid="${department.id}">
							<div class="header">${department.title}</div>
							<ul>
								<c:forEach items="${clubMemberList}" var="member">
									<c:if test="${member.department.id == department.id}">
										<li<c:if test="${member.user.id == sessionScope.USER_SESSION.id}"> class="self"</c:if> data-cmid="${member.id}"><span class="member-name" title="${member.user.nickname}">${member.name}</span><span class="member-contact" title="联系方式">${member.contact}</span></li>
									</c:if>
								</c:forEach>
							</ul>
						</div>
					</c:forEach>
					<div class="default">
						<div class="header">未分组</div>
						<ul>
							<c:forEach items="${defaultMembers}" var="member">
								<li<c:if test="${member.user.id == sessionScope.USER_SESSION.id}"> class="self"</c:if> data-cmid="${member.id}"><span class="member-name" title="${member.user.nickname}">${member.name}</span><span class="member-contact" title="联系方式">${member.contact}</span></li>
							</c:forEach>
						</ul>
					</div>
					<div class="add-new-department">
						<button class="btn btn-primary sb" onclick="javascript:editDepartment()">增加部门</button>
						<div class="department-edit hb">
							<input type="text" class="form-control" id="new-department" placeholder="XX部（组）" maxlength="20">
							<button class="btn btn-success" onclick="javascript:addDepartment()">确认</button>
							<button class="btn btn-danger" onclick="javascript:cancelDepartment()">取消</button>
						</div>
					</div>
				</div>
			</div>
			<button class="btn btn-warning" id="load-more">移交社长权限</button>
			<div id="more">
				<div class="well" id="manager-candidate">
					<div class="item">
						<c:forEach items="${clubMemberList}" var="member">
							<div class="radio overflow-ellipsis<c:if test="${member.user.status != 1}"> disabled</c:if>">
								<label title="${member.user.nickname}">
									<input type="radio" name="candidate" value="${member.id}"<c:if test="${member.id == club.manager.id}"> checked="checked"</c:if><c:if test="${member.user.status != 1}"> disabled="disabled"</c:if>>
									${member.name}
								</label>
							</div>
						</c:forEach>
					</div>
					<div class="default">
						<c:forEach items="${defaultMembers}" var="member">
							<div class="radio overflow-ellipsis default<c:if test="${member.user.status != 1}"> disabled</c:if>">
								<label title="${member.user.nickname}">
									<input type="radio" name="candidate" value="${member.id}"<c:if test="${member.id == club.manager.id}"> checked="checked"</c:if><c:if test="${member.user.status != 1}"> disabled="disabled"</c:if>>
									${member.name}
								</label>
							</div>
						</c:forEach>
					</div>
				</div>
				<input type="password" id="password-validate" class="form-control" data-display=0 placeholder="输入登陆密码以继续">
				<br>
				<button class="btn btn-danger" onclick="javascript:changeManager()">更换社长</button>
			</div>
		</div>
	</div>
</div>
<jsp:include page="../include/include_navbar.jsp" flush="true"/>
<script type="text/javascript"	src="<%=basepath%>/js/jquery.form.min.js"></script>
<script type="text/javascript">
var g_clubId = $("#clubid").val();
(function() {
	$(".left-wrapper, .right-wrapper").mCustomScrollbar( {
		theme: "minimal-dark",
		axis: "y"
	});

	$("#badge-form").ajaxForm({
		beforeSerialize: function() {
			$("#badge-progress-bar").html(0).width(0);
			var filePath = $("#badge-form #badge").val();
			var fileType = filePath.substring(filePath.lastIndexOf("."), filePath.length).toUpperCase();
			if(fileType != ".PNG") {
				alert("请选择PNG格式图片上传");
				return false;
			}
			$("#badge-progress-bar").addClass("active");
		},
		uploadProgress: function(event, position, total, percentComplete) {
			var percentVal = percentComplete + "%";
			$("#badge-progress-bar").html(percentVal).width(percentVal);
		},
		success: function(json) {
			switch (json.code) {
				case -2:
					alert("未指定更新类别，请刷新页面重试");
					break;
				case -1:
					alert("无更新权限，请刷新页面重试");
					break;
				case 0:
					alert("社团数据出错");
					break;
				case 1:
					// append random number so that the badge can be refreshed
					$(".club-badge img").attr("src", "<%=basepath%>/" + json.newBadge + "?token=" + Math.random().toString(10));
					alert("上传新团徽成功");
					break;
				default:
					alert("上传图片出错");
			}
			$("#badge-progress-bar").removeClass("active");
		}
	});
	$("#photo-form").ajaxForm({
		beforeSerialize: function() {
			$("#photo-progress-bar").html(0).width(0);
			var filePath = $("#photo-form #photo").val();
			var fileType = filePath.substring(filePath.lastIndexOf("."), filePath.length).toUpperCase();
			if(fileType != ".PNG") {
				alert("请选择PNG格式图片上传");
				return false;
			}
			$("#photo-progress-bar").addClass("active");
		},
		uploadProgress: function(event, position, total, percentComplete) {
			var percentVal = percentComplete + "%";
			$("#photo-progress-bar").html(percentVal).width(percentVal);
		},
		success: function(json) {
			switch (json.code) {
				case -2:
					alert("未指定更新类别，请刷新页面重试");
					break;
				case -1:
					alert("无更新权限，请刷新页面重试");
					break;
				case 0:
					alert("社团数据出错");
					break;
				case 1:
					if ($("#photo-form input[name='type']").val() == "whole") {
						$("#whole-photo img").attr("src", "<%=basepath%>/" + json.newPhoto + "?token=" + Math.random().toString(10));
					}
					else if ($("#photo-form input[name='type']").val() == "newbie") {
						$("#newbie-photo img").attr("src", "<%=basepath%>/" + json.newPhoto + "?token=" + Math.random().toString(10));
					}
					else {
						alert("上传图片失败");
					}
					break;
				default:
					alert("上传图片出错");
			}
			$("#badge-progress-bar").removeClass("active");
		}
	});
	
	$("a[data-toggle='tab']").on("show.bs.tab", function (e) {
		$("#photo-form input[type='submit']").val("上传" + $(e.target).text());
		$("#photo-form input[name='type']").val($(e.target).attr("data-type"));
	});
	
	// fill year list
	var startYear = 2010;
	var endYear = new Date().getFullYear();
	for (var i=endYear; i>=startYear; i--) {
		$("#member-year").append("<option value=" + i + ">" + i + "</option>");
	}
	
	// interaction for choosing group by clicking year
	$("#group-year span").click(function(e) {
		if ($(this).attr("class") == "item") {
			// reset all span's class
			$("#group-year span").attr("class", "item");
			// dealing the clicked one
			$(this).attr("class", "active");
			loadGroup($(this).attr("data-year"));
		}
	});
	
	$("#load-more").click(function(e) {
		$(this).hide(200);
		$("#more").show(200);
	});
	
})();

function changeName() {
	$(".club-name .sb").hide();
	$(".club-name .hb").show();
}
function doChangeName() {
	var cnName = $("#cn-name").val().trim();
	var enName = $("#en-name").val().trim();
	if (cnName.length == 0) {
		alert("社团名称不能为空");
		return;
	}
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/club/manage/update_club",
		dataType: "JSON",
		data: {
			clubId: g_clubId,
			cnName: cnName,
			enName: enName,
			type: 1
		}
	}).done(function(data) {
		switch (data.code) {
			case -1:
				alert("无更新权限，请刷新页面重试");
				break;
			case 0:
				alert("社团数据出错");
				break;
			case 1:
				$(".club-name .sb").show();
				$(".club-name .hb").hide();
				$(".club-name .cn-name").html(cnName);
				if (enName.length > 0) {
					$(".club-name .en-name").html(enName);
				}
				else {
					$(".club-name .en-name").hide();
				}
				break;
			default:
				alert("保存失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
function cancelChangeName() {
	$(".club-name .sb").show();
	$(".club-name .hb").hide();
	$("#cn-name").val($(".club-name .cn-name").html());
	$("#en-name").val($(".club-name .en-name").html());
	if ($("#en-name").val().trim().length == 0) {
		$(".club-name .en-name").hide();
	}
}
function editIntro() {
	$("#intro").attr("disabled", false);
	$(".club-intro .sb").hide();
	$(".club-intro .hb").show();
}
function updateIntro() {
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/club/manage/update_club",
		dataType: "JSON",
		data: {
			clubId: g_clubId,
			intro: $("#intro").val().trim(),
			type: 2
		}
	}).done(function(data) {
		switch (data.code) {
			case -2:
				alert("未指定更新类别，请刷新页面重试");
				break;
			case -1:
				alert("无更新权限，请刷新页面重试");
				break;
			case 0:
				alert("社团数据出错");
				break;
			case 1:
				$("#intro").attr("disabled", true);
				$(".club-intro .sb").show();
				$(".club-intro .hb").hide();
				break;
			default:
				alert("保存失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
function addTag() {
	var newTag = "<div class='item'>";
	newTag += "<span class='name'>";
	newTag += "<input type='text' maxlength=25>";
	newTag += "</span>";
	newTag += "<span class='glyphicon glyphicon-ok ope' onclick='javscript:saveTag($(this))'></span>";
	newTag += "</div>";
	$("#tags .add-tag").before(newTag);
}
function saveTag(ele) {
	var name = $(ele).prev().children("input[type='text']").val().trim();
	if (name.length == 0) {
		alert("请输入标签内容");
		return;
	}
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/club/manage/update_club",
		dataType: "JSON",
		data: {
			clubId: g_clubId,
			isAddTag: true,
			tag: name,
			type: 3
		}
	}).done(function(data) {
		switch (data.code) {
			case -2:
				alert("未指定更新类别，请刷新页面重试");
				break;
			case -1:
				alert("无更新权限，请刷新页面重试");
				break;
			case 0:
				alert("社团数据出错");
				break;
			case 1:
				$(ele).attr("class", "glyphicon glyphicon-remove ope").attr("onclick", "javascript:removeTag('" + data.newTag.id + "')");
				$(ele).prev().html(data.newTag.name);
				$(ele).parent().attr("data-tagid", data.newTag.id);
				break;
			case 2:
				alert("标签内容重复");
				$(ele).prev().children("input[type='text']").focus();
				break;
			default:
				alert("保存失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
function removeTag(tagid) {
	if (tagid == undefined) {
		alert("未指定标签");
		return;
	}
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/club/manage/update_club",
		dataType: "JSON",
		data: {
			clubId: g_clubId,
			isAddTag: false,
			tag: tagid,
			type: 3
		}
	}).done(function(data) {
		switch (data.code) {
			case -2:
				alert("未指定更新类别，请刷新页面重试");
				break;
			case -1:
				alert("无更新权限，请刷新页面重试");
				break;
			case 0:
				alert("社团数据出错");
				break;
			case 1:
				$("#tags .item[data-tagid='" + tagid + "']").remove();
				break;
			default:
				alert("保存失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
function editDepartment() {
	$(".add-new-department .sb").hide();
	$(".add-new-department .hb").show();
	$("#new-department").focus();
}
function addDepartment() {
	var title = $("#new-department").val().trim();
	if (title.length == 0) {
		alert("请填写部门名称");
		return;
	}
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/club/manage/add_department",
		dataType: "JSON",
		data: {
			clubId: g_clubId,
			title: title
		}
	}).done(function(data) {
		switch (data.code) {
			case -1:
				alert("无此权限，请刷新页面重试");
				break;
			case 0:
				alert("社团数据出错");
				break;
			case 1:
				var newDepartment = "<div class='item' data-deptid='" + data.newDepartmentId + "'>";
				newDepartment += "<div class='header'>";
				newDepartment += data.newDepartmentTitle;
				newDepartment += "</div>";
				newDepartment += "<ul></ul>";
				newDepartment += "</div>";
				$(".departments .default").before(newDepartment);
				// add new option for department choosing
				$("#member-department").append("<option value=" + data.newDepartmentId + ">" + data.newDepartmentTitle + "</option>");
				// hide editting block
				cancelDepartment();
				break;
			default:
				alert("添加失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
function cancelDepartment() {
	$(".add-new-department .sb").show();
	$(".add-new-department .hb").hide();
	$("#new-department").val("");
}
function addMember() {
	var nickname = $("#member-nickname").val().trim();
	if (nickname.length == 0) {
		alert("请输入昵称");
		return;
	}
	var year = $("#member-year").val();
	var departmentId = $("#member-department").val();
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/club/manage/add_club_member",
		dataType: "JSON",
		data: {
			clubId: g_clubId,
			nickname: nickname,
			year: year,
			departmentId: departmentId
		}
	}).done(function(data) {
		switch (data.code) {
			case -3:
				alert("无此部门");
				break;
			case -2:
				alert("查无此人！！！");
				$("#member-nickname").focus();
				break;
			case -1:
				alert("无此权限，请刷新页面重试");
				break;
			case 0:
				alert("社团数据出错");
				break;
			case 1:
				if (data.isNewGroup || data.isDeleteGroup) {		// refresh page if created a new group
					location.reload();
					return;					// in case, you know!
				}
				// clear old data
				$(".departments ul li[data-cmid=" + data.clubMemberId + "]").remove();
				var newMember = "<li data-cmid='" + data.clubMemberId + "'>";
				newMember += "<span class='member-name' title='" + nickname + "'>" + nickname + "</span>"
				newMember += "<span class='member-contact' title='联系方式'></span>"
				newMember += "</li>";
				if (data.isDefault) {
					$(".departments .default ul").append(newMember);
				}
				else {
					$(".departments[data-gpid='" + data.groupId + "'] .item[data-deptid='" + data.departmentId + "'] ul").append(newMember);
				}
				$("#member-nickname").val("");
				// automatically switch to the year block of the new-added member
				if ($("#group-year .active").attr("data-year") != year) {
					$("#group-year span[data-year=" + year + "]").click();
				}
				break;
			default:
				alert("添加失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
function loadGroup(year) {
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/club/load_group",
		dataType: "JSON",
		data: {
			clubId: g_clubId,
			year: $("#group-year .active").attr("data-year"),
		}
	}).done(function(data) {
		if (data.code == 1) {
			$("#whole-photo img").attr("src", "<%=basepath%>/" + data.wholePhoto);
			$("#newbie-photo img").attr("src", "<%=basepath%>/" + data.newbiePhoto);
			$(".departments").attr("data-gpid", data.groupId);
			// refresh department member list
			$(".departments .item ul li").remove();
			// refresh candidate list
			$("#manager-candidate .item").html("");
			var cmItem = "";
			var canItem = "";
			$.each(data.clubMembers, function(i, e) {
				cmItem = "<li data-cmid='" + e.clubMemberId + "'><span class='member-name' title='" + e.userNickname + "'>" + e.name + "</span><span class='member-contact' title='联系方式'>" + e.contact + "</span></li>";
				$(".departments[data-gpid='" + data.groupId + "'] .item[data-deptid='" + e.departmentId + "'] ul").append(cmItem);
				if (e.isRestricted) {
					canItem = "<div class='radio overflow-ellipsis disabled'>";
				}
				else {
					canItem = "<div class='radio overflow-ellipsis'>";
				}
				canItem += "<label title='" + e.userNickname + "'>";
				if (e.isManager) {
					canItem += "<input type='radio' name='candidate' value='" + e.clubMemberId + "' checked='checked'";
					if (e.isRestricted) {
						canItem += " disabled='disabled'";
					}
					canItem += ">"
				}
				else {
					canItem += "<input type='radio' name='candidate' value='" + e.clubMemberId + "'";
					if (e.isRestricted) {
						canItem += " disabled='disabled'";
					}
					canItem += ">"
				}
				canItem += e.name;
				canItem += "</label>"
				canItem += "</div>"
				$("#manager-candidate .item").append(canItem);
			});
		}
		else {
			alert("加载分组信息时出错");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
function changeManager() {
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
		url: "<%=basepath%>/club/manage/change_manager",
		dataType: "JSON",
		data: {
			clubId: g_clubId,
			newManagerId: $("#manager-candidate input[type='radio'][name='candidate']:checked").val(),
			password: password
		}
	}).done(function(data) {
		switch (data.code) {
			case -3:
				alert("密码错误");
				break;
			case -2:
				alert("成员信息错误");
				break;
			case -1:
				alert("无此权限，请刷新页面重试");
				break;
			case 0:
				alert("社团数据出错");
				break;
			case 1:
				alert("权限移交成功");
				window.location.href = "<%=basepath%>/user/club/" + g_clubId;
				break;
			default:
				alert("授权失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
</script>
</body>
</html>