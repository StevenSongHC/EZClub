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
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/user-info-add-and-set.css">
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/bootstrap-datepicker.css">
<title>用户设置 | EZ Club</title>
</head>
<body>
<div id="main">
	<div id="index-sticker" class="rounded-border">
		<a href="<%=basepath%>"><img alt="EZClub" src="<%=basepath%>/images/site/logo.jpg"></a>
	</div>
	<h1>EZ Club 个人信息答卷</h1>
	<h3>答题卡</h3>
	<br>
	<br>
	<br>
	<input type="hidden" id="user-nickname" value="${user.nickname}">
	<div class="user-portrait">
		<img src="<%=basepath%>/${user.photo}" id="user-photo">
		<form action="<%=basepath%>/user/upload_portrait" id="portrait-form" method="post" enctype="multipart/form-data">
			<input type="file" name="portraitFile" id="portrait">
			<input type="submit" value="上传头像" class="btn btn-default">
			<input type="hidden" name="fileType" value=".png">
		</form>
		<div class="progress">
			<div id="portrait-progress-bar" class="progress-bar" role="progressbar" style="width: 0%">
				<span></span>
			</div>
		</div>
	</div>
	<div class="user-account">
		<div class="label unselectable">昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称</div>
		<span class="alert" id="fail-nickname" style="left: -200px;">请输入尚未被他人使用的昵称</span>
		<input type="text" id="nickname" maxlength="20" value="${user.nickname}">
	</div>
	<div id="user-more">
		<table class="table table-bordered" id="info-marker">
			<thead>
				<tr>
					<th>个人信息</th>
					<th>一</th>
					<th>二</th>
					<th>三</th>
					<th>四</th>
					<th>五</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th scope="row">得分情况</th>
					<td><span class="glyphicon glyphicon-ok" id="sex-info"></span></td>
					<td><span class="glyphicon glyphicon-ok" id="birth-info"></span></td>
					<td><span class="glyphicon glyphicon-ok" id="position-info"></span></td>
					<td><span class="glyphicon glyphicon-ok" id="interest-info"></span></td>
					<td><span class="glyphicon glyphicon-ok" id="photo-info"></span></td>
				</tr>
			</tbody>
		</table>
		<hr>
		<div id="user-info-block" style="display: block;">
			<div class="user-info">
				1.你的性别是
				<select id="sex">
					<option value="0" <c:if test="${user.sex == 0}">selected="selected"</c:if>>神秘</option>
					<option value="1" <c:if test="${user.sex == 1}">selected="selected"</c:if>>男</option>
					<option value="2" <c:if test="${user.sex == 2}">selected="selected"</c:if>>女</option>
				</select>
				。
			</div>
			<div class="user-info">
				2.你的生日是在
				<input id="birth" class="span2" size="16" type="text" data-date="1990-01-01" data-date-formate="yyyy-mm-dd" value="<fmt:formatDate pattern='yyyy-MM-dd' value='${user.birthDate}'/>" readOnly>
				。（<a href="javascript:clearBirth()">点此清空</a>）
			</div>
			<div class="user-info">
				3.你的大学是？（<a href="<%=basepath%>/create_college" target="_blank">没有？点此添加）</a>
				<br>
				<select id="province">
					<option value="0" <c:if test="${user.province == null}">selected="selected"</c:if>>省份
					<c:forEach items="${provinceList}" var="province">
						<option value="${province.id}" <c:if test="${user.province.id == province.id}">selected="selected"</c:if>>${province.cnName}</option>
					</c:forEach>
				</select>
				<select id="city" <c:if test="${empty cityList}">disabled="disabled"</c:if>>
					<option value="0" <c:if test="${user.city == null}">selected="selected"</c:if>>城市</option>
					<c:if test="${not empty cityList}">
						<c:forEach items="${cityList}" var="city">
							<option value="${city.id}" <c:if test="${user.city.id == city.id}">selected="selected"</c:if>>${city.cnName}</option>
						</c:forEach>
					</c:if>
				</select>
				<select id="college" <c:if test="${empty collegeList}">disabled="disabled"</c:if>>
					<option value="0" <c:if test="${user.college == null}">selected="selected"</c:if>>大学</option>
					<c:if test="${not empty collegeList}">
						<c:forEach items="${collegeList}" var="college">
							<option value="${college.id}" <c:if test="${user.college.id == college.id}">selected="selected"</c:if>>${college.cnName}</option>
						</c:forEach>
					</c:if>
				</select>
			</div>
			<div class="user-info">
				4.你有什么兴趣爱好？
				<br>
				<c:forEach items="${user.interests}" var="interest">
					<input type="text" class="interest-item" value="${interest.name}"><span class="delete-interest glyphicon glyphicon-minus" onclick="javascript:deleteInterest(this)"></span>
				</c:forEach>
				<span class="add-interest glyphicon glyphicon-plus" onclick="javascript:addInterest()"></span>
			</div>
		</div>
		<hr>
		<button id="submit-join" class="btn btn-default btn-lg" onclick="javascript:submitUpdate()">交卷</button>
	</div>
</div>

<jsp:include page="../include/include_js.jsp" flush="true"/>
<script type="text/javascript"	src="<%=basepath%>/js/bootstrap-datepicker.js"></script>
<script type="text/javascript"	src="<%=basepath%>/js/jquery.form.min.js"></script>
<script type="text/javascript">
var g_interestCount = $("input[type='text'].interest-item").length;
(function() {
	if ($("#sex").val() !== "0") {
		$("#sex-info").show();
	}
	if ($("#birth").val() !== "") {
		$("#birth-info").show();
	}
	if ($("#college").val() !== "0") {
		$("#position-info").show();
	}
	if ($("#user-photo").attr("src") !== "<%=basepath%>/images/portrait/default.png") {
		$("#photo-info").show();
	}
	if (g_interestCount > 0) {
		$("#interest-info").show();
	}
	
	$("#birth").datepicker()
		.on("changeDate", function(e) {
			// birth info hint display
			if (e.date.valueOf() !== "") {
				$("#birth-info").show(200);
			}
		});
	$("#sex").change(function(e) {
		if ($(this).val() !== "0") {
			$("#sex-info").show(200);
		}
		else {
			$("#sex-info").hide();
		}
	});
	$("#province").change(function(e) {
		if ($(this).val() != "0") {
			$("#city option:not(:first)").remove();
			$("#college").attr("disabled", "disabled").val("0");
			loadCityListByProvince($(this).val());
		}
		else {
			$("#city").attr("disabled", "disabled").val("0");
			$("#college").attr("disabled", "disabled").val("0");
			$("#position-info").hide();
		}
	});
	$("#city").change(function(e) {
		if ($(this).val() != "0") {
			$("#college option:not(:first)").remove();
			loadCollegeListByCity($(this).val());
		}
		else {
			$("#college").attr("disabled", "disabled").val("0");
			$("#position-info").hide();
		}
	});
	$("#college").change(function(e) {
		if ($(this).val() !== "0") {
			$("#position-info").show(200);
		}
		else {
			$("#position-info").hide();
		}
	});
	
	$("#portrait-form").ajaxForm({
		beforeSerialize: function() {
			$("#portrait-progress-bar").html(0).width(0);
			var filePath = $("#portrait-form #portrait").val();
			var fileType = filePath.substring(filePath.lastIndexOf("."), filePath.length).toUpperCase();
			if(fileType != ".PNG") {
				alert("请选择PNG格式图片上传");
				return false;
			}
			$("#portrait-progress-bar").addClass("active");
		},
		uploadProgress: function(event, position, total, percentComplete) {
			var percentVal = percentComplete + "%";
			$("#portrait-progress-bar").html(percentVal).width(percentVal);
		},
		success: function(json) {
			if (json.code == 1) {
				$("#user-photo").attr("src", "<%=basepath%>/" + json.newPhoto + "?token=" + Math.random().toString(10));
				alert("头像上传成功");
			}
			else {
				alert("头像保存失败");
			}
			$("#portrait-progress-bar").removeClass("active");
		}
	});
})();

function isNicknameExisted(nickname) {
	// nickname remians the same
	if (nickname === $("#user-nickname").val()) {
		return false;
	}
	
	var isExisted = true;
	$.ajax( {
		async: false,
		type: "POST",
		url: "<%=basepath%>/ajax/is_nickname_existed",
		dataType: "JSON",
		data: {
			nickname: nickname
		}
	}).done(function(data) {
		if (data.code === 0) {
			isExisted = false;
		}
	}).fail(function() {
		alert("与服务器通信失败");
	}).error(function (XMLHttpRequest, textStatus, errorThrown) {
		alert("服务器错误");
	});
	return isExisted;
}

function clearBirth() {
	$("#birth").val("");
	$("#birth-info").hide();
}
function addInterest() {
	var newInterest = "<input type='text' class='interest-item'>";
	newInterest += "<span class='delete-interest glyphicon glyphicon-minus' onclick='javascript:deleteInterest(this)'></span>"
	$(".add-interest").before(newInterest);
	++g_interestCount;
	// interest info hint display
	$("#interest-info").show(200);
}
function deleteInterest(e) {
	$(e).prev().remove();
	$(e).remove();
	if (--g_interestCount <= 0) {
		$("#interest-info").hide();
	}
}

function submitUpdate() {
	$(".alert").css("visibility", "hidden");
	var nickname = $("#nickname").val().trim();
	var sex = $("#sex").val();
	var birth = $("#birth").val();
	var province = $("#province").val();
	var city = $("#city").val();
	var college = $("#college").val();
	
	var interArr = new Array();
	$(".interest-item").each(function(i, e) {
		var inter = $(this).val().trim();
		if (inter != null) {
			// delete repeated interest item
			if (interArr.indexOf(inter) == -1)
				interArr.push(inter);
			else {
				$(this).prev().remove();
				$(this).remove();
			}
		}
		else {
			$(this).prev().remove();
			$(this).remove();
		}
	});
	
	if (nickname.length > 0 && !isNicknameExisted(nickname)) {
		$.ajax( {
			async: true,
			type: "POST",
			url: "<%=basepath%>/user/update",
			dataType: "JSON",
			data: {
				nickname: nickname,
				sex: sex,
				birth: birth,
				provinceId: province,
				cityId: city,
				collegeId: college,
				"interests[]": interArr
			}
		}).done(function(data) {
			if (data.code == 1) {
				$("#user-nickname").val(nickname);
				alert("保存成功");
			}
			else {
				alert("保存失败");
			}
		}).fail(function() {
			alert("保存失败");
		});
	}
	else {
		$("#fail-nickname").css("visibility", "visible");
		$("#nickname").focus();
	}
}
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
</script>
</body>
</html>