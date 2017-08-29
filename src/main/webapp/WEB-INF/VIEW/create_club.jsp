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
<jsp:include page="include/include_style.jsp" flush="true"/>
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/create-club.css">
<title>新建社团 | EZ Club</title>
</head>
<body>
<div id="main">
	<h3>选择地点</h3>
	<div class="club-info">
		<select id="province">
			<option value="0">省份（必选）</option>
			<c:forEach items="${provinceList}" var="pv">
				<option value="${pv.id}">${pv.cnName}</option>
			</c:forEach>
		</select>
		<select id="city" disabled="disabled">
			<option value="0">城市（必选）</option>
		</select>
	</div>
	<h3>选择学校</h3>
	<div class="club-info">
		<select id="college" disabled="disabled">
			<option value="0">选择学校（必选）</option>
		</select>
		<a href="<%=basepath%>/create_college" target="_blank">没有？点此新建</a>
	</div>
	<h3>社团信息</h3>
	<div class="club-info">
		<input type="text" id="cn-name" class="form-control" placeholder="社团名字（必填）" maxlength="12">
		<input type="text" id="en-name" class="form-control" placeholder="社团高大上的英文名">
		<div class="intro-block">
			<div class="intro-header">社团简介</div>
			<textarea class="form-control" rows="7" id="intro"></textarea>
		</div>
	</div>
	<button class="btn btn-default btn-lg" id="submit-btn" onclick="javascript:submit()">提交</button>
</div>

<jsp:include page="include/include_navbar.jsp" flush="true"/>
<script type="text/javascript">
(function() {
	$("#province").change(function(e) {
		if ($(this).val() != "0") {
			$("#city option:not(:first)").remove();
			loadCityListByProvince($(this).val());
		}
		else {
			$("#city").attr("disabled", "disabled").val("0");
			$("#college").attr("disabled", "disabled").val("0");
		}
	});
	$("#city").change(function(e) {
		if ($(this).val() != "0") {
			$("#college option:not(:first)").remove();
			loadCollegeListByCity($(this).val());
		}
		else {
			$("#college").attr("disabled", "disabled").val("0");
		}
	});
})();

function loadCityListByProvince(province) {
	$.ajax( {
		async: true,
		url: "<%=basepath%>/ajax/load_city_list_by_province",
		type: "POST",
		dataType: "JSON",
		data: {
			province: province
		}
	}).done(function(data) {
		$.each(data.list, function(i, ct) {
			$("#city").append("<option value='" + ct.id + "'>" + ct.cnName + "</option>");
		});
		$("#city").removeAttr("disabled");
	}).fail(function() {
		alert("加载城市列表数据出错");
	});
}
function loadCollegeListByCity(city) {
	$.ajax( {
		async: true,
		url: "<%=basepath%>/ajax/load_college_list_by_city",
		type: "POST",
		dataType: "JSON",
		data: {
			city: city
		}
	}).done(function(data) {
		$.each(data.list, function(i, clg) {
			$("#college").append("<option value='" + clg.id + "'>" + clg.cnName + "</option>");
		});
		$("#college").removeAttr("disabled");
	}).fail(function() {
		alert("加载城市列表数据出错");
	});
}

function submit() {
	var province = $("#province").val();
	var city = $("#city").val();
	var college = $("#college").val();
	var clubCnName = $("#cn-name").val().trim();
	var clubEnName = $("#en-name").val().trim();
	var intro = $("#intro").val().trim();
	if (province == "0") {
		alert("请选择省份");
		return;
	}
	if (city == "0") {
		alert("请选择城市");
		return;
	}
	if (college == "0") {
		alert("请选择学校");
		return;
	}
	if (clubCnName.length < 3) {
		alert("请输入长度至少3位的社团名字");
		return;
	}
	
	$.ajax( {
		async: true,
		url: "<%=basepath%>/create_club.do",
		type: "POST",
		dataType: "JSON",
		data: {
			province: province,
			city: city,
			college: college,
			clubCnName: clubCnName,
			clubEnName: clubEnName,
			intro: intro
		}
	}).done(function(data) {
		switch (data.code) {
			case -4:
				alert("省份信息出错");
				$("#province").val("0");
				$("#city").attr("disabled", "disabled").val("0");
				$("#college").attr("disabled", "disabled").val("0");
				break;
			case -3:
				alert("城市信息出错");
				$("#city").val("0");
				$("#college").attr("disabled", "disabled").val("0");
				break;
			case -2:
				alert("学校信息出错");		
				$("#college").val("0");
				break;
			case -1:
				alert("已存在名为【" + clubCnName + "】的社团");
				break;
			case 1:
				alert("新建成功，请等待管理员审核通过 :)");
				window.close();
				window.location.href = "<%=basepath%>";
				break;
			default:
				alert("新建失败");
		}
	}).fail(function() {
		alert("操作失败");
	});
}
</script>
</body>
</html>