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
<title>学校数据 | EZ Club</title>
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
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>名称</th>
						<th>英文名</th>
						<th>首字母</th>
						<th>社团数</th>
						<th>提交时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${checkedCollegeList}" var="cc">
						<tr>
							<th scope="row">${cc.cnName}</th>
							<td>${cc.enName}</td>
							<td>${cc.shortName}</td>
							<td>${cc.data.clubCount}</td>
							<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${cc.createDate}"/></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="tab-pane fade <c:if test="${uncheckedAmount != 0}">in active</c:if>" id="unchecked">
			<c:forEach items="${uncheckedCollegeList}" var="uc">
				<div class="item-block" data-clgid="${uc.id}">
					<div class="left-wrapper">
						<div class="item">
							<input type="text" class="form-control cn-name" value="${uc.cnName}">
						</div>
						<div class="item">
							<input type="text" class="form-control en-name" value="${uc.enName}">
						</div>
						<div class="item">
							<input type="text" class="form-control short-name" value="${uc.shortName}">
						</div>
						<div class="item">
							提交于<u><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${uc.createDate}"/></u>
						</div>
						<div class="item">
							<button class="btn btn-default" onclick="javascript:doCheckCollege('${uc.id}', true)">通过</button>
							<button class="btn btn-default" onclick="javascript:doCheckCollege('${uc.id}', false)">隐藏</button>
						</div>
					</div>
					<div class="middle-wrapper">
						<div class="intro-header">学校简介&nbsp;（${uc.city.province.cnName}>${uc.city.cnName}）</div>
						<textarea class="form-control intro" rows="9">${uc.intro}</textarea>
					</div>
					<div class="right-wrapper">
						<div class="badge-pic">
							<img src="<%=basepath%>/${uc.badge}">
						</div>
						<div class="photo-pic">
							<img src="<%=basepath%>/${uc.photo}">
						</div>
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
	$("#admin-college").addClass("active");
})();

function doCheckCollege(clgid, isPass) {
	var ele = $(".item-block[data-clgid='" + clgid + "']");
	var cnName = $(ele).find(".cn-name").val().trim();
	var enName = $(ele).find(".en-name").val().trim();
	var shortName = $(ele).find(".short-name").val().trim();
	var intro = $(ele).find(".intro").val().trim();
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/admin/update_college.do",
		dataType: "JSON",
		data: {
			clgid: clgid,
			isPass: isPass,
			cnName: cnName,
			enName: enName,
			shortName: shortName,
			intro: intro
		}
	}).done(function(data) {
		switch (data.code) {
			case -2:
				alert("大学ID出错");
				break;
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