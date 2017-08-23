<%@ page language="java" contentType="text/html; charset=GB2312"
	pageEncoding="GB2312"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<title>Insert title here</title>
<script language="javascript">
	function onclickAjax() {
		var xmlHttp;
		//分浏览器创建XMLHttp对象
		if (window.XMLHttpRequest) {
			xmlHttp = new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP")
		}
		//设置请求类型
		xmlHttp
				.open("POST", "test.do?method=ajaxTest&&msg=" + new Date(),
						true);
		//回调函数
		xmlHttp.onreadystatechange = function() {
			if (xmlHttp.readyState == 4) {
				if (xmlHttp.status == 200) {
					document.getElementById("testid").value = xmlHttp.responseText;
				} else {
					alert("AJAX服务器返回错误！");
				}
			}
		}
		//发送请求
		xmlHttp.send();
	}
</script>
</head>
<body>
	<form action="SearchServlet" method="post">
		<input type="text" name="searchstr"/><br> 
		<input type="text" name="first"/><br>
		<input type="text" name="last"/><br>
		<input type="submit" value="提交"/>
	</form>
</body>
</html>