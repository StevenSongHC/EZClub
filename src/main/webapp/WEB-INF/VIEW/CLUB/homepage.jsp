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
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/club-homepage.css">
<title>${club.cnName} | EZ Club</title>
</head>
<body>
<div id="main" style="padding-top: 105px; padding-left: 60px;">
	<div class="tab-content">
		<div class="tab-pane fade in active" id="whole-photo">
			<img src="<%=basepath%>/${defaultGroup.wholePhoto}"/>
		</div>
		<div class="tab-pane fade" id="newbie-photo">
			<img src="<%=basepath%>/${defaultGroup.newbiePhoto}"/>
		</div>
	</div>
	<div class="groups">
		<ul class="nav nav-tabs">
			<li class="active"><a href="#whole-photo" data-toggle="tab" data-type="whole">全体合照</a></li>
			<li><a href="#newbie-photo" data-toggle="tab" data-type="newbie">新生合照</a></li>
		</ul>
		<div id="group-year"<c:if test="${empty groupList}">style="display: none;"</c:if>>
			<c:forEach items="${groupList}" var="group" varStatus="status">
				<c:choose>
					<c:when test="${status.count == 1}">
						<span class="active" data-year=${group.year}>${group.year}</span>
					</c:when>
					<c:otherwise>
						<span class="item<c:if test="${empty sessionScope.USER_SESSION}"> empty</c:if>" data-year=${group.year}>${group.year}</span>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<div style="clear: both;"></div>
		</div>
	</div>
	<select id="member-list">
		<option selected="selected">新成员名单</option>
		<c:forEach items="${clubMemberList}" var="member">
			<option>${member.name}</option>
		</c:forEach>
	</select>
	<div id="club-label" class="row">
		<div class="col-md-1 bage">
			<img src="<%=basepath%>/${club.badge}" class="img-thumbnail">
			<div id="btn-group">
				<c:choose>
					<c:when test="${isSubscribed}">
						<a id="btn-unsubscribe" class="btn btn-default btn-sm" href="javascript:unsubscribe('${club.id}')">已关注</a>
					</c:when>
					<c:otherwise>
						<a id="btn-subscribe" class="btn btn-primary btn-sm" href="javascript:subscribe('${club.id}')">关注</a>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="col-md-4" style="padding-left: 30px;">
			<div class="position">
				<span>${club.college.city.province.cnName}</span>
				<span>${club.college.city.cnName}</span>
				<span>${club.college.cnName}</span>
			</div>
			<div class="club-cn-name">
				${club.cnName}
			</div>
			<div class="statistics">
				<span>成员数：${memberCount}</span>
				<span>活动数：${activityCount}</span>
				<span>关注数：${subscriptionCount}</span>
			</div>
		</div>
		<div class="col-md-4 col-md-offset-1">
			<dl class="dl-horizontal">
				<dt>标签</dt>
				<dd>
					<c:forEach items="${club.tags}" var="tag">
						<span class="tag-item">${tag.name}</span>
					</c:forEach>
				</dd>
			</dl>
		</div>
	</div>
	<hr>
	<div class="activities">
		<ul class="nav nav-tabs">
			<li class="active"><a href="#activity-list" data-toggle="tab">活动列表</a></li>
			<li><a href="#club-intro" data-toggle="tab">社团介绍</a></li>
		</ul>
		<div class="tab-content">
			<div class="tab-pane fade in active" id="activity-list">
				<c:forEach items="${activityList}" var="activity">
					<div class="activity-item overflow-ellipsis">
						<a href="<%=basepath%>/activity/${activity.id}" target="_blank" title="${activity.title}&nbsp;<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${activity.updateDate}"/>">${activity.title}</a>
						<span class="activity-date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${activity.updateDate}"/></span>
					</div>
				</c:forEach>
			</div>
			<div class="tab-pane fade" id="club-intro">
				<div class="jumbotron">
					<p>${club.intro}</p>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="../include/include_navbar.jsp" flush="true"/>
<script type="text/javascript">
(function() {
	$("#group-year span").click(function(e) {
		if ($(this).attr("class") == "item") {
			// reset all span's class
			$("#group-year span").attr("class", "item");
			// dealing the clicked one
			$(this).attr("class", "active");
			loadGroup($(this).attr("data-year"));
		}
	});
	$("a#btn-unsubscribe").hover(function() {
		$(this).text("取消关注");
	}, function() {
		$(this).text("已关注");
	});
})();
function loadGroup(year) {
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/club/load_group",
		dataType: "JSON",
		data: {
			clubId: "${club.id}",
			year: $("#group-year .active").attr("data-year"),
		}
	}).done(function(data) {
		if (data.code == 1) {
			$("#whole-photo img").attr("src", "<%=basepath%>/" + data.wholePhoto);
			$("#newbie-photo img").attr("src", "<%=basepath%>/" + data.newbiePhoto);
			$("#member-list>option").not(":first").remove();
			$.each(data.clubMembers, function(i, e) {
				$("#member-list").append("<option>" + e.clubMemberName + "</option>");
			});
		}
		else {
			alert("加载分组信息时出错");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
function subscribe(clubId) {
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/club/subscribe",
		dataType: "JSON",
		data: {
			clubId: clubId
		}
	}).done(function(data) {
		switch (data.code) {
			case -2:
				alert("你已订阅该社团");
				break;
			case -1:
				alert("清先登录");
				break;
			case 0:
				alert("社团数据数据出错");
				window.location.reload();
				break;
			case 1:
				var btn = "<a id=\"btn-unsubscribe\" class=\"btn btn-default btn-sm\" href=\"javascript:unsubscribe('" + clubId + "')\">已关注</a>";;
				$("#btn-group").html(btn);

				$("a#btn-unsubscribe").hover(function() {
					$(this).text("取消关注");
				}, function() {
					$(this).text("已关注");
				});
				break;
			default:
				alert("操作失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
function unsubscribe(clubId) {
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/club/unsubscribe",
		dataType: "JSON",
		data: {
			clubId: clubId
		}
	}).done(function(data) {
		switch (data.code) {
			case -2:
				alert("你并未订阅该社团");
				break;
			case -1:
				alert("清先登录");
				break;
			case 0:
				alert("社团数据数据出错");
				window.location.reload();
				break;
			case 1:
				var btn = "<a id=\"btn-subscribe\" class=\"btn btn-primary btn-sm\" href=\"javascript:subscribe('" + clubId + "')\">关注</a>";
				$("#btn-group").html(btn);
				break;
			default:
				alert("操作失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
</script>
</body>
</html>