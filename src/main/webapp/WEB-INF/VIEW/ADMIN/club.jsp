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
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/admin-style.css">
<title>社团数据 | EZ Club</title>
</head>
<body>
<div id="main">
	<div class="switch-tab">
		<ul id="tab" class="nav nav-tabs">
			<c:choose>
				<c:when test="${uncheckedAmount == 0}">
					<li class="active">
						<a href="#checked" data-toggle="tab">已审核</a>
					</li>
					<li>
						<a href="#unchecked" data-toggle="tab">未审核</a>
					</li>
				</c:when>
				<c:otherwise>
					<li>
						<a href="#checked" data-toggle="tab">已审核</a>
					</li>
					<li class="active">
						<a href="#unchecked" data-toggle="tab">未审核<span class="badge">${uncheckedAmount}</span></a>
					</li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
	<div id="college-list" class="tab-content">
		<div class="tab-pane fade <c:if test="${uncheckedAmount == 0}">in active</c:if>" id="checked">
			<table class="table table-bordered" style="width: 90%; margin-left: 0px;">
				<thead>
					<tr>
						<th>名称</th>
						<th>团徽</th>
						<th>学校</th>
						<th>社长</th>
						<th>成员数</th>
						<th>活动数</th>
						<th>关注数</th>
						<th>标签</th>
						<th>注册时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${checkedClubList}" var="cc">
						<tr>
							<th scope="row"><a href="<%=basepath%>/c/${cc.id}" target="_blank">${cc.cnName}</a></th>
							<td><img src="<%=basepath%>/${cc.badge}" width="60px" height="60px"></td>
							<td>${cc.college.cnName}</td>
							<td><a href="<%=basepath%>/u/${cc.manager.user.id}" target="_blank">${cc.manager.user.nickname}</a></td>
							<td>${cc.data.memberCount}</td>
							<td>${cc.data.activityCount}</td>
							<td>${cc.data.subscriptionCount}</td>
							<td>
								<select>
								<c:forEach items="${cc.tags}" var="tag">
									<option value="${tag.id}">${tag.name}</option>
								</c:forEach>
								</select>
							</td>
							<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${cc.createDate}"/></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="tab-pane fade <c:if test="${uncheckedAmount != 0}">in active</c:if>" id="unchecked">
			<c:forEach items="${uncheckedClubList}" var="uc">
				<div class="item-block" data-cid="${uc.id}">
					<div class="left-wrapper">
						<div class="item">
							<input type="text" class="form-control cn-name" value="${uc.cnName}">
						</div>
						<div class="item">
							<input type="text" class="form-control en-name" value="${uc.enName}">
						</div>
						<div class="item">
							${uc.college.cnName}
						</div>
						<div class="item">
							<u>${uc.manager.user.nickname}</u>提交于<br><u><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${uc.createDate}"/></u>
						</div>
						<div class="item">
							<button class="btn btn-default" onclick="javascript:doCheckClub('${uc.id}', true)">通过</button>
							<button class="btn btn-default" onclick="javascript:doCheckClub('${uc.id}', false)">隐藏</button>
						</div>
					</div>
					<div class="middle-wrapper">
						<div class="intro-header">学校简介</div>
						<textarea class="form-control intro" rows="9">${uc.intro}</textarea>
					</div>
				</div>
				<div style="clear: both;"></div>
			</c:forEach>
		</div>
	</div>
</div>

<jsp:include page="../include/include_navbar.jsp" flush="true"/>
<script type="text/javascript">
(function () {
	$("#admin-club").addClass("active");
})();

function doCheckClub(cid, isPass) {
	var ele = $(".item-block[data-cid='" + cid + "']");
	var cnName = $(ele).find(".cn-name").val().trim();
	var enName = $(ele).find(".en-name").val().trim();
	var intro = $(ele).find(".intro").val().trim();
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/admin/update_club.do",
		dataType: "JSON",
		data: {
			cid: cid,
			isPass: isPass,
			cnName: cnName,
			enName: enName,
			intro: intro
		}
	}).done(function(data) {
		switch (data.code) {
			case -1:
				alert("已存在名为【" + cnName + "】的记录");
				break;
			case 1:
				alert("操作成功");
				$(ele).remove();
				break;
			default:
				
		}
	}).fail(function() {
		alert("操作失败");
	}).error(function (XMLHttpRequest, textStatus, errorThrown) {
		alert("服务器出错");
	});
}
</script>
</body>
</html>