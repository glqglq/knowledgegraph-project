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
		if (window.XMLHttpRequest) {
			xmlHttp = new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlHttp.open("POST", "SearchServlet?searchstr=" + searchstr + "&&msg="
				+ new Date(), false);
		xmlHttp.onreadystatechange = function(searchstr) {
			if (xmlHttp.readyState == 4) {
				if (xmlHttp.status == 200) {
					document.getElementById("testid").value = xmlHttp.responseText;
				} else {
					alert("AJAX");
				}
			}
		}
		xmlHttp.send();
	}
</script>
</head>
<body>
	<form action="SearchServlet" method="post">
		<input type="text" name="searchstr"><br> <input
			type="submit" value="提交"
			onclick="onclickAjax(document.getElementByName('searchstr').innerHTML)">
	</form>
</body>
</html>