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
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/create-college.css">
<title>添加学校 | EZ Club</title>
</head>
<body>
<div id="main">
	<form id="new-college" enctype="multipart/form-data" action="<%=basepath%>/create_college.do" class="form-inline" method="POST">
		<h3>选择地点</h3>
		<div class="position">
			<div class="position-block">
				<select class="province">
					<option value="0">选择省份（必选）</option>
					<c:forEach items="${provinceList}" var="pv">
						<option value="${pv.id}">${pv.cnName}</option>
					</c:forEach>
				</select>
				<input type="text" class="form-control province" placeholder="XX+省" readOnly>
				<div class="check-box">
					<label class="unselectable">
						<input type="checkbox" class="province">手动新建
					</label>
				</div>
			</div>
			<div class="position-block">
				<select class="city" disabled="disabled">
					<option value="0">选择城市（必选）</option>
				</select>
				<input type="text" class="form-control city" placeholder="XX+市" readOnly>
				<div class="check-box">
					<label class="unselectable">
						<input type="checkbox" class="city">手动新建
					</label>
				</div>
			</div>
		</div>
		<div style="clear:both;"></div>
		<h3>新建学校</h3>
		<div class="college">
			<div class="college-block">
				<div class="college-item">
					<div class="form-group">
						<label for="cn-name">学校名</label>
						<input type="text" class="form-control" name="college-cn-name" id="cn-name" placeholder="必填">
					</div>
				</div>
				<div class="college-item">
					<div class="form-group">
						<label for="en-name">英文名</label>
						<input type="text" class="form-control" name="college-en-name">
					</div>
				</div>
				<div class="college-item">
					<div class="form-group">
						<label for="cn-name">首字母</label>
						<input type="text" class="form-control" name="college-short-name">
					</div>
				</div>
				<div class="college-item">
					<div class="form-group">
						<input type="file" id="badge" name="badge">
						<p class="help-block">上传校徽</p>
					</div>
				</div>
				<div class="college-item">
					<div class="form-group">
						<input type="file" id="photo" name="photo">
						<p class="help-block">上传学校大图（正门）</p>
					</div>
				</div>
			</div>
			<div class="college-block">
				<div class="intro-block">
					<div class="intro-header">学校简介</div>
					<textarea class="form-control" name="intro" rows="10" cols="35" id="intro"></textarea>
				</div>
			</div>
		</div>
		<div style="clear: both;"></div>
		<button type="submit" class="btn btn-default btn-lg" id="submit-btn">提交</button>
		<div class="progress">
			<div id="upload-progress-bar" class="progress-bar" role="progressbar" style="width: 0%">
				<span></span>
			</div>
		</div>
		<input type="hidden" name="province">
		<input type="hidden" name="is-new-province">
		<input type="hidden" name="city">
		<input type="hidden" name="is-new-city">
		<input type="hidden" name="file-type" value=".png">
	</form>
</div>

<jsp:include page="include/include_navbar.jsp" flush="true"/>
<script type="text/javascript"	src="<%=basepath%>/js/jquery.form.min.js"></script>
<script type="text/javascript">
(function () {
	$("select.province").change(function(e) {
		if ($(this).val() != "0") {
			// clear old data
			$("select.city option:not(:first)").remove();
			loadCityListByProvince($(this).val());
		}
		else {
			$("select.city").attr("disabled", "disabled");
		}
	});
	
	$("input[type='checkbox'].province").click(function() {
		if ($("input[type='checkbox'].province:checked").val() === "on") {
			$("input[type='text'].province").attr("readOnly", false);
			$("select.province").attr("disabled", "disabled").val("0");
			$("input[type='checkbox'].city").click();
		}
		else {
			$("input[type='text'].province").attr("readOnly", true).val("");
			$("select.province").removeAttr("disabled");
		}
	});
	$("input[type='checkbox'].city").click(function() {
		if ($("input[type='checkbox'].city:checked").val() === "on") {
			$("input[type='text'].city").attr("readOnly", false);
			$("select.city").attr("disabled", "disabled").val("0");
		}
		else {
			$("input[type='text'].city").attr("readOnly", true).val("");
			$("select.city").removeAttr("disabled");
		}
	});
	
	$("form#new-college").ajaxForm({
		beforeSerialize: function() {
			$("#upload-progress-bar").html(0).width(0);

			// get position
			var isNewProvince = false;
			var isNewCity = false;
			var province = $("select.province").val();
			var city = $("select.city").val();
			if (province == "0") {
				isNewProvince = true;
				province = $("input[type='text'].province").val().trim();
				if (province.length === 0) {
					alert("请填入省份");
					return false;
				}
			}
			if (city == "0") {
				isNewCity = true;
				city = $("input[type='text'].city").val().trim();
				if (city.length === 0) {
					alert("请填入城市");
					return false;
				}
			}
			// fill em up
			$("input[name='province']").val(province);
			$("input[name='is-new-province']").val(isNewProvince);
			$("input[name='city']").val(city);
			$("input[name='is-new-city']").val(isNewCity);
			// get colleg cn_name
			var collegeCnName = $("#cn-name").val().trim();
			if (collegeCnName.length < 2) {
				alert("请输入的学校名长度大于2（全称）");
				return false;
			}
			$("#cn-name").val(collegeCnName);
			
			var badgePath = $("input[type='file']#badge").val();
			var photoPath = $("input[type='file']#photo").val();
			// upload badge
			if (badgePath.length > 0) {
				var badgeType = badgePath.substring(badgePath.lastIndexOf("."), badgePath.length).toUpperCase();
				if(badgeType != ".PNG") {
					alert("请选择PNG格式图片");
					return false;
				}
			}
			if (photoPath.length > 0) {
				var photoType = photoPath.substring(photoPath.lastIndexOf("."), photoPath.length).toUpperCase();
				if(photoType != ".PNG") {
					alert("请选择PNG格式图片");
					return false;
				}
			}
			
			$("#upload-progress-bar").addClass("active");
		},
		uploadProgress: function(event, position, total, percentComplete) {
			var percentVal = percentComplete + "%";
			$("#upload-progress-bar").html(percentVal).width(percentVal);
		},
		success: function(data) {
			switch (data.code) {
				case -3:
					alert("省份数据出错");
					location.reload();
					break;
				case -2:
					alert("城市数据出错");
					location.reload();
					break;
				case -1:
					alert("该学校已存在");
					$("#cn-name").focus();
					break;
				case 1:
					alert("添加成功，你现在可以去选择你的学校了");
					$("#new-college")[0].reset();
					window.close();
					break;
				default:
					alert("服务器出错");
			}
			$("#upload-progress-bar").removeClass("active");
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
			$("select.city").append("<option value='" + ct.id + "'>" + ct.cnName + "</option>");
		});
		$("select.city").removeAttr("disabled");
	}).fail(function() {
		alert("加载城市列表数据出错");
	});
}
</script>
</body>
</html>